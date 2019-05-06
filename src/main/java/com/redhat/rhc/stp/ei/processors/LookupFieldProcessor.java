package com.redhat.rhc.stp.ei.processors;

import java.util.Map;

import org.apache.camel.Exchange;

import com.redhat.rhc.stp.ei.CamelHelper;
import com.redhat.rhc.stp.ei.configuration.Configuration;
import com.redhat.rhc.stp.ei.configuration.ConfigurationService;

/**
 * Field processor setting values based on cache lookups of existing values. Each cache has a
 * set of key field names. The values for each of these key fields are used to lookup additional
 * values from the configured cache. Each cache value field name is set as a new value name with
 * its corresponding value from the cache entry looked up.
 * 
 * In case a cache lookup fails, the cache lookup key can be used as the new value(s) instead by
 * setting the defaultToLookup property to true. Alternately, if a cache lookup fails, the
 * processing can be ignored entirely by setting the ignoreFailedLookup property, resulting in
 * no new values added.
 */
public class LookupFieldProcessor extends AbstractFieldProcessor {

	public LookupFieldProcessor() {
	}

	private String cacheName;
	private Boolean defaultToLookup = Boolean.FALSE;
	private Boolean ignoreFailedLookup = Boolean.FALSE;

	/**
	 * Sets the cache name to be used for the lookup
	 * 
	 * @param cacheName the name of the registered cache
	 */
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName; 
	}

	/**
	 * Gets the cache name to be used for the lookup
	 * 
	 * @return the name of the registered cache
	 */
	public String getCacheName() {
		return cacheName;
	}
	
	/**
	 * Sets whether to store the cache lookup key as the value(s) should the cache
	 * lookup fail to find that key. Both defaultToLookup and ignoreFailedLookup
	 * cannot be set to true concurrently, however both can be false to do nothing
	 * on a failed cache lookup. Defaults to false.
	 *   
	 * @param defaultToLookup the String to parse as a Boolean, following Boolean.valueOf(String) semantics
	 */
	public void setDefaultToLookup(String defaultToLookup) {
		this.defaultToLookup = Boolean.valueOf(defaultToLookup);
		if(this.defaultToLookup) {
			this.ignoreFailedLookup = Boolean.FALSE;
		}
	}

	/**
	 * Sets whether a failed cache lookup throws an IllegalStateException when not also defaulting
	 * to storing the cache lookup key as the value(s). Both defaultToLookup and ignoreFailedLookup
	 * cannot be set to true concurrently, however both can be false to do nothing on a failed cache
	 * lookup. Defaults to false.
	 * 
	 * @param ignoreFailedLookup the String to parse as a Boolean, following Boolean.valueOf(String) semantics
	 */
	public void setIgnoreFailedLookup(String ignoreFailedLookup) {
		this.ignoreFailedLookup = Boolean.valueOf(ignoreFailedLookup);
		if(this.ignoreFailedLookup) {
			this.defaultToLookup = Boolean.FALSE;
		}
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// Grab the ConfigurationService and Configuration for the lookup
		ConfigurationService configurationService = CamelHelper.getCamelContextObject(ConfigurationService.CONFIGURATION_SERVICE_BEAN_NAME, ConfigurationService.class, exchange);
		Configuration configuration = configurationService.getConfiguration(cacheName);

		// If no cache exists with that name, we have a bad configuration and need to tell someone
		if(configuration == null) {
			throw new IllegalStateException("Unable to find cache named " + cacheName + " processing lookup");
		}
		
		// Loop through the Configuration keyFieldNames to build the cache lookup key
		// from existing field values
		StringBuilder cacheLookupKeyBuilder = new StringBuilder();
		for(String cacheKeyFieldName : configuration.getKeyFieldNames()) {
			// Get the existing field value for this cache keyFieldName
			String cacheKeyFieldValue = exchange.getIn().getHeader(cacheKeyFieldName, String.class);
			
			// If no field value exists, just use a "*"--maybe someone can use this to say
			// an existing field value doesn't matter for this lookup
			if(cacheKeyFieldValue == null || cacheKeyFieldValue.isEmpty())
				cacheKeyFieldValue = "*";

			// There are already field values in the StringBuilder, add a '|' as the separator
			if(cacheLookupKeyBuilder.length() > 0)
				cacheLookupKeyBuilder.append('|');

			// Add the field value to the StringBuilder
			cacheLookupKeyBuilder.append(cacheKeyFieldValue);
		}

		// This cache lookup key from the StringBuilder
		String cacheLookupKey = cacheLookupKeyBuilder.toString();

		// Get the Configuration again just for the lookup key
		Configuration configurationValue = configurationService.getConfigurationValue(cacheName, cacheLookupKey);

		if(configurationValue != null && !configurationValue.getConfigurationValues().isEmpty()) {
			// If we have a cache value map
			Map<String, String> cacheValueMap = configurationValue.getConfigurationValues().get(0).getValue();

			// Map<String, Object>.putAll(Map<String, String>) no worky so we have to code the loop
			for(Map.Entry<String, String> cacheValueEntry : cacheValueMap.entrySet()) {
				// Loop through the entries adding each new field
				exchange.getIn().setHeader(cacheValueEntry.getKey(), cacheValueEntry.getValue());
			}
		} else {
			if(defaultToLookup) {
				// If a cache lookup fails, set each cache value field to be the original cache lookup key
				// This is unlikely to be needed in real situations
				for(String cacheValueFieldName : configuration.getValueFieldNames()) {
					exchange.getIn().setHeader(cacheValueFieldName, cacheLookupKey);
				}
			} else if(!ignoreFailedLookup) {
				// If a cache lookup failure doesn't result in setting the cache lookup key as the field value(s)
				// and failed lookups are not ignored, a failed lookup is fatal. Tell someone. 
				throw new IllegalStateException("Unable to lookup required mapping for input value " + cacheLookupKey + " on cache " + cacheName);
			}
		}
	}
}