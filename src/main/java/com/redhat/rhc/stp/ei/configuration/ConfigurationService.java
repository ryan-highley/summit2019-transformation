package com.redhat.rhc.stp.ei.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.redhat.rhc.stp.ei.configuration.persistence.model.Cache;
import com.redhat.rhc.stp.ei.configuration.persistence.model.CacheValue;
import com.redhat.rhc.stp.ei.configuration.persistence.service.CachePersistenceService;

public class ConfigurationService {

	public static final String CONFIGURATION_SERVICE_BEAN_NAME = "ConfigurationService";

	public ConfigurationService() {
	}

	private CachePersistenceService cachePersistenceService;

	public CachePersistenceService getCachePersistenceService() {
		return cachePersistenceService;
	}

	public void setCachePersistenceService(CachePersistenceService cachePersistenceService) {
		this.cachePersistenceService = cachePersistenceService;
	}

	public Set<String> getAllNames() {
		return cachePersistenceService.getAllCacheNames();
	}

	public Configuration getConfiguration(String configurationName) {
		Cache cache = cachePersistenceService.getCache(configurationName);
		if (cache == null)
			return null;

		return populateConfiguration(cache);
	}

	public Configuration getConfigurationValue(String configurationName, String configurationKey) {
		// Let's get wild
		List<String> parsedKey = Cache.parse(configurationKey);

		List<String> wildcardKeys = ConfigurationService.getWildcardKeys(parsedKey);

		// Check the original configurationKey first--don't bother checking
		// again
		wildcardKeys.remove(configurationKey);

		List<String> configurationKeys = new ArrayList<>(wildcardKeys.size() + 1);
		configurationKeys.add(configurationKey);
		configurationKeys.addAll(wildcardKeys);

		for (String key : configurationKeys) {
			Cache cache = cachePersistenceService.getCacheValue(configurationName, key);

			if (cache != null) {
				return populateConfiguration(cache);
			}
		}

		return null;
	}

	private Configuration populateConfiguration(Cache cache) {
		Configuration configuration = new Configuration();

		configuration.setName(cache.getName());
		configuration.setKeyFieldNames(cache.getKeyFieldNames());
		configuration.setValueFieldNames(cache.getValueFieldNames());

		List<CacheValue> cacheValues = cache.getValues();
		List<ConfigurationValue> configurationValues = new ArrayList<>(cacheValues.size());

		for (CacheValue cacheValue : cacheValues) {
			ConfigurationValue configurationValue = new ConfigurationValue();

			configurationValue.setConfigurationName(configuration.getName());

			configurationValue.setKeyFieldNames(configuration.getKeyFieldNames());
			configurationValue.setKey(cacheValue.getKey());

			configurationValue.setValueFieldNames(configuration.getValueFieldNames());
			configurationValue.setValue(cacheValue.getValues());

			configurationValues.add(configurationValue);
		}

		configuration.setConfigurationValues(configurationValues);

		return configuration;
	}

	public static final List<String> getWildcardKeys(List<String> remainingParsedKey) {
		List<String> wildcardKeys = new ArrayList<>();

		String head = remainingParsedKey.get(0);
		if (remainingParsedKey.size() == 1) {
			wildcardKeys.add(head);
			wildcardKeys.add("*");
			return wildcardKeys;
		}

		List<String> tail = remainingParsedKey.subList(1, remainingParsedKey.size());
		List<String> childWildcardKeys = getWildcardKeys(tail);
		for (String keyPart : new String[] { head, "*" }) {
			for (String childWildcardKey : childWildcardKeys) {
				wildcardKeys.add(keyPart + "|" + childWildcardKey);
			}
		}

		return wildcardKeys;
	}
}