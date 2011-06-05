package com.hexaforge.util;

import java.util.Collections;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

public class MemcacheConnector {

	private Cache cache;
	private boolean isInit = false;

	private static final String MEMCACHE_PREFIX = "1";

	public MemcacheConnector() throws MemcacheConnectorException,
			CacheException {
		cache = CacheManager.getInstance().getCacheFactory()
				.createCache(Collections.emptyMap());
		isInit = true;
	}

	public String getString(Object key) {
		return ((isInit) ? (String) get(key) : null);
	}

	public byte[] getByteArray(Object key) {
		return ((isInit) ? (byte[]) get(key) : null);
	}

	private Object get(Object key) {
		return ((isInit) ? cache.get(MEMCACHE_PREFIX + "_" + key) : null);
	}

	public void put(Object key, Object value) {
		cache.put(MEMCACHE_PREFIX + "_" + key, value);
	}
}
