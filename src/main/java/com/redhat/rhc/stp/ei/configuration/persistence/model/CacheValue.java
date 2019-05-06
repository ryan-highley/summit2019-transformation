package com.redhat.rhc.stp.ei.configuration.persistence.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

public class CacheValue {

	@XmlElement
	private Integer id;
	
	@XmlElement
	private Map<String, String> key;
	
	@XmlElement
	private Map<String, String> values;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Map<String, String> getKey() {
		return Collections.unmodifiableMap(key);
	}
	public void setKey(Map<String, String> key) {
		this.key = new HashMap<>(key);
	}
	public Map<String, String> getValues() {
		return Collections.unmodifiableMap(values);
	}
	public void setValues(Map<String, String> values) {
		this.values = new HashMap<>(values);
	}
}
