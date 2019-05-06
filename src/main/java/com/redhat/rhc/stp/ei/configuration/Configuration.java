package com.redhat.rhc.stp.ei.configuration;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

	public Configuration() {
	}

	private String name;
	private List<String> keyFieldNames;
	private List<String> valueFieldNames;
	private List<ConfigurationValue> configurationValues;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getKeyFieldNames() {
		return new ArrayList<>(keyFieldNames);
	}

	public void setKeyFieldNames(List<String> keyFieldNames) {
		this.keyFieldNames = new ArrayList<>(keyFieldNames);
	}

	public List<String> getValueFieldNames() {
		return new ArrayList<>(valueFieldNames);
	}

	public void setValueFieldNames(List<String> valueFieldNames) {
		this.valueFieldNames = new ArrayList<>(valueFieldNames);
	}

	public List<ConfigurationValue> getConfigurationValues() {
		return new ArrayList<>(configurationValues);
	}

	public void setConfigurationValues(List<ConfigurationValue> configurationValues) {
		this.configurationValues = new ArrayList<>(configurationValues);
	}
}
