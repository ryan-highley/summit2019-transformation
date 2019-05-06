package com.redhat.rhc.stp.ei.configuration.persistence.service;

import java.util.List;
import java.util.Set;

import com.redhat.rhc.stp.ei.configuration.persistence.model.Cache;
//import com.redhat.rhc.stp.ei.configuration.persistence.model.CacheFieldMetadata;

public interface CachePersistenceService {

	public Cache getCache(String cacheName);
	
	public Cache getCache(Integer cacheId);

	public Set<String> getAllCacheNames();
	
	public Cache getCacheValue(String cacheName, String cacheKey);
	
	public Cache getCacheValue(Integer cacheValueId);
	
	public Cache createCache(Cache cache);

	public Cache updateCache(Cache cache);
	
	public Cache deleteCache(Cache cache);
	
	public Cache createCacheValue(Cache cache);
	
	public Cache updateCacheValue(Cache cache);
	
	public Cache deleteCacheValue(Cache cache);
	
//	public List<CacheFieldMetadata> getAllFieldMetadata();
//
//	public CacheFieldMetadata getFieldMetadata(String fieldName);
//
//	public CacheFieldMetadata createFieldMetadata(CacheFieldMetadata cacheFieldMetadata);
//	
//	public CacheFieldMetadata updateFieldMetadata(CacheFieldMetadata cacheFieldMetadata);
//	
//	public CacheFieldMetadata deleteFieldMetadata(CacheFieldMetadata cacheFieldMetadata);
}
