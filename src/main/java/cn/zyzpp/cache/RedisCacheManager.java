package cn.zyzpp.cache;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * Create by yster@foxmail.com 2018-05-15 11:26:24
**/
public class RedisCacheManager implements CacheManager {
	
	@Resource
	private RedisCache redisCache;

	@Override
	public <K, V> Cache<K, V> getCache(String s) throws CacheException {

		return redisCache;
	}

}
