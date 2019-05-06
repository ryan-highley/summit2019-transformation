package com.redhat.rhc.stp.ei.configuration.persistence.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

//import com.dteet.esb.core.data.ets_datasource_jpa.entities.EtsCache;
//import com.dteet.esb.core.data.ets_datasource_jpa.entities.EtsCacheValue;

@XmlRootElement
public class Cache {

	@XmlElement
	private Integer id;

	@XmlElement
	private String name;

	@XmlTransient
	private List<String> keyFieldNames;

	@XmlElement
	private List<CacheFieldMetadata> keyFields;
	
	@XmlTransient
	private List<String> valueFieldNames;

	@XmlElement
	private List<CacheFieldMetadata> valueFields;
	
	@XmlElement
	private List<CacheValue> values;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getKeyFieldNames() {
		return keyFieldNames;
	}

	public List<CacheFieldMetadata> getKeyFields() {
		return keyFields;
	}
	
	public void setKeyFields(List<CacheFieldMetadata> keyFields) {
		this.keyFields = Collections.unmodifiableList(keyFields);
		this.keyFieldNames = getNamesFromMetadata(keyFields);
	}
	
	public List<String> getValueFieldNames() {
		return valueFieldNames;
	}

	public List<CacheFieldMetadata> getValueFields() {
		return valueFields;
	}
	
	public void setValueFields(List<CacheFieldMetadata> valueFields) {
		this.valueFields = Collections.unmodifiableList(valueFields);
		this.valueFieldNames = getNamesFromMetadata(valueFields);
	}
	
	public List<CacheValue> getValues() {
		return values;
	}

	public void setValues(List<CacheValue> values) {
		this.values = Collections.unmodifiableList(values);
	}
	
	private static List<String> getNamesFromMetadata(List<CacheFieldMetadata> fieldMetadata) {
		List<String> fieldNames = new ArrayList<>(fieldMetadata.size());
		
		for(CacheFieldMetadata fieldMetadatum : fieldMetadata) {
			fieldNames.add(fieldMetadatum.getName());
		}
		
		return Collections.unmodifiableList(fieldNames);
	}

//	public static Cache fromEtsCache(EtsCache etsCache) {
//		if (etsCache == null)
//			return null;
//
//		Cache cache = new Cache();
//		cache.setId(etsCache.getCacheId());
//		cache.setName(etsCache.getCacheName());
//
//		List<String> keyFieldNames = Cache.parse(etsCache.getCacheKeyFieldNames());
//		List<String> valueFieldNames = Cache.parse(etsCache.getCacheValueFieldNames());
//
//		List<CacheFieldMetadata> keyFieldMetadata = new ArrayList<>(keyFieldNames.size());
//		for(String keyFieldName : keyFieldNames) {
//			CacheFieldMetadata keyFieldMetadatum = new CacheFieldMetadata();
//			keyFieldMetadatum.setName(keyFieldName);
//			keyFieldMetadata.add(keyFieldMetadatum);
//		}
//		cache.setKeyFields(keyFieldMetadata);
//		
//		List<CacheFieldMetadata> valueFieldMetadata = new ArrayList<>(valueFieldNames.size());
//		for(String valueFieldName : valueFieldNames) {
//			CacheFieldMetadata valueFieldMetadatum = new CacheFieldMetadata();
//			valueFieldMetadatum.setName(valueFieldName);
//			valueFieldMetadata.add(valueFieldMetadatum);
//		}
//		cache.setValueFields(valueFieldMetadata);
//
//		List<EtsCacheValue> etsCacheValues = etsCache.getEtsCacheValues();
//		if (etsCacheValues != null) {
//			List<CacheValue> cacheValues = new ArrayList<>(etsCacheValues.size());
//			for (EtsCacheValue etsCacheValue : etsCacheValues) {
//				CacheValue cacheValue = new CacheValue();
//
//				cacheValue.setId(etsCacheValue.getCacheValueId());
//				cacheValue.setKey(Cache.parseValue(keyFieldNames, etsCacheValue.getCacheKey()));
//				cacheValue.setValues(Cache.parseValue(valueFieldNames, etsCacheValue.getCacheValue()));
//
//				cacheValues.add(cacheValue);
//			}
//			cache.setValues(cacheValues);
//		}
//
//		return cache;
//	}
//	
//	public EtsCache toEtsCache() {
//		EtsCache etsCache = new EtsCache();
//		
//		if(this.getId() != null)
//			etsCache.setCacheId(this.getId());
//		
//		etsCache.setCacheName(this.getName());
//
//		List<String> keyFieldNames = new ArrayList<>(this.getKeyFields().size());
//		for(CacheFieldMetadata keyFieldMetadatum : this.getKeyFields()) {
//			keyFieldNames.add(keyFieldMetadatum.getName());
//		}
//		List<String> valueFieldNames = new ArrayList<>(this.getValueFields().size());
//		for(CacheFieldMetadata valueFieldMetadatum : this.getValueFields()) {
//			valueFieldNames.add(valueFieldMetadatum.getName());
//		}
//		
//		etsCache.setCacheKeyFieldNames(Cache.join(keyFieldNames));
//		etsCache.setCacheValueFieldNames(Cache.join(valueFieldNames));
//		
//		List<EtsCacheValue> etsCacheValues = new ArrayList<>(this.getValues().size());
//		for(CacheValue cacheValue : this.getValues()) {
//			EtsCacheValue etsCacheValue = new EtsCacheValue();
//			
//			if(cacheValue.getId() != null)
//				etsCacheValue.setCacheValueId(cacheValue.getId());
//
//			etsCacheValue.setCacheKey(Cache.join(cacheValue.getKey(), keyFieldNames));
//			etsCacheValue.setCacheValue(Cache.join(cacheValue.getValues(), valueFieldNames));
//			etsCacheValue.setEtsCache(etsCache);
//			
//			etsCacheValues.add(etsCacheValue);
//		}
//		etsCache.setEtsCacheValues(etsCacheValues);
//		
//		return etsCache;
//	}

	public static final Map<String, String> parseValue(List<String> fieldNames, String rawValue) {
		Map<String, String> values = new HashMap<>();

		List<String> parsedFieldValues = Cache.parse(rawValue);
		for (int i = 0; i < parsedFieldValues.size(); i++) {
			String fieldValue = parsedFieldValues.get(i);

			String fieldName = null;
			try {
				fieldName = fieldNames.get(i);
			} catch (IndexOutOfBoundsException | NullPointerException e) {
				fieldName = "FIELD_" + i;
			}

			values.put(fieldName, fieldValue);
		}

		return Collections.unmodifiableMap(values);
	}

	public static final List<String> parse(String rawValue) {
		String[] valuesArr = rawValue.split("(?<!\\\\)\\|");
		List<String> parsedValues = new ArrayList<>(valuesArr.length);
		for (int i = 0; i < valuesArr.length; i++) {
			String fieldValue = valuesArr[i].replaceAll("\\\\(.)", "$1");
			parsedValues.add(fieldValue);
		}
		return Collections.unmodifiableList(parsedValues);
	}
	
	public static String join(List<String> stringList) {
		StringBuilder sb = new StringBuilder();
		for(String s : stringList) {
			sb.append(s);
			sb.append('|');
		}

		sb.setLength(sb.length() - 1); // Drop last '|'
		return sb.toString();
	}
	
	public static String join(Map<String, String> valueMap, List<String> nameList) {
		StringBuilder sb = new StringBuilder();
		for(String name : nameList) {
			String value = valueMap.get(name);
			if(value == null)
				value = "";

			sb.append(value);
			sb.append('|');
		}
		
		sb.setLength(sb.length() - 1); // Drop last '|'
		return sb.toString();
	}
}
