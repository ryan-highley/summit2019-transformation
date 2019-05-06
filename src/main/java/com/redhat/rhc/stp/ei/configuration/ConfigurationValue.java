package com.redhat.rhc.stp.ei.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationValue {

	public ConfigurationValue() {
	}

	private String configurationName;
	private List<String> keyFieldNames;
	private List<String> valueFieldNames;
	private Map<String, String> key;
	private Map<String, String> value;

	public String getConfigurationName() {
		return configurationName;
	}

	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

	public String getRawKey() {
		StringBuilder sb = new StringBuilder();
		for (String keyFieldName : keyFieldNames) {
			String keyField = key.get(keyFieldName);
			if (keyField == null)
				keyField = "";

			sb.append(keyField);
			sb.append('|');
		}
		sb.setLength(sb.length() - 1); // Drop the last '|'
		return sb.toString();
	}

	public Map<String, String> getKey() {
		return Collections.unmodifiableMap(key);
	}

	public void setKey(Map<String, String> key) {
		this.key = new HashMap<>(key);
	}

	public Map<String, String> getValue() {
		return Collections.unmodifiableMap(value);
	}

	public void setValue(Map<String, String> value) {
		this.value = new HashMap<>(value);
	}

	public List<String> getKeyFieldNames() {
		return Collections.unmodifiableList(keyFieldNames);
	}

	public void setKeyFieldNames(List<String> keyFieldNames) {
		this.keyFieldNames = new ArrayList<>(keyFieldNames);
	}

	public List<String> getValueFieldNames() {
		return Collections.unmodifiableList(valueFieldNames);
	}

	public void setValueFieldNames(List<String> valueFieldNames) {
		this.valueFieldNames = new ArrayList<>(valueFieldNames);
	}
}
