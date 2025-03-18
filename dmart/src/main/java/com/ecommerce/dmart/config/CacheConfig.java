package com.ecommerce.dmart.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

public class CacheConfig {
	
	 @Bean
	    public CacheManager cacheManager(RedisConnectionFactory factory) {
	        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
	            .entryTtl(Duration.ofMinutes(180)) // Set TTL for cache
	            .disableCachingNullValues();
	        return RedisCacheManager.builder(factory)
	            .cacheDefaults(config)
	            .build();
	    }

}
