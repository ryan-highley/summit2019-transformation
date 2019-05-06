package com.redhat.rhc.stp.ei.configuration.persistence.model;

import javax.xml.bind.annotation.XmlElement;

//import com.dteet.esb.core.data.ets_datasource_jpa.entities.Ets_CacheFieldMetadata;

public class CacheFieldMetadata {

	public CacheFieldMetadata() {
	}

	@XmlElement
	private String name;
	
	@XmlElement
	private String type;
	
	@XmlElement
	private String uri;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
//	public static CacheFieldMetadata fromEtsCacheFieldMetadata(Ets_CacheFieldMetadata etsCacheFieldMetadata) {
//		if(etsCacheFieldMetadata == null)
//			return null;
//		
//		CacheFieldMetadata cacheFieldMetadata = new CacheFieldMetadata();
//		
//		cacheFieldMetadata.setName(etsCacheFieldMetadata.getFieldName());
//		cacheFieldMetadata.setType(etsCacheFieldMetadata.getFieldType());
//		cacheFieldMetadata.setUri(etsCacheFieldMetadata.getUri());
//		
//		return cacheFieldMetadata;
//	}
//	
//	public Ets_CacheFieldMetadata toEtsCacheFieldMetadata() {
//		
//		Ets_CacheFieldMetadata etsCacheFieldMetadata = new Ets_CacheFieldMetadata();
//		
//		etsCacheFieldMetadata.setFieldName(this.getName());
//		etsCacheFieldMetadata.setFieldType(this.getType());
//		etsCacheFieldMetadata.setUri(this.getUri());
//		
//		return etsCacheFieldMetadata;
//	}
}
