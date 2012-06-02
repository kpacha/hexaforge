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

	public String getString(Object key) throws MemcacheKeyNotFoundException {
		return ((isInit) ? (String) get(key) : null);
	}

	public byte[] getByteArray(Object key) throws MemcacheKeyNotFoundException {
		return ((isInit) ? (byte[]) get(key) : null);
	}

	private Object get(Object key) throws MemcacheKeyNotFoundException {
		Object result = ((isInit) ? cache.get(MEMCACHE_PREFIX + "_" + key) : null);
		if (result == null)
			throw new MemcacheKeyNotFoundException();
		return result;
	}

	@SuppressWarnings("unchecked")
	public void put(Object key, Object value) {
		cache.put(MEMCACHE_PREFIX + "_" + key, value);
	}
}
