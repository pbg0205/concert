package com.cooper.concert.storage.caffenine.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class LocalCacheConfig {

	@Bean
	public CacheManager caffeineCacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager("concertSchedules");
		cacheManager.setCaffeine(caffeineCacheBuilder());
		cacheManager.registerCustomCache("concertSchedules", concertSchedulesBuilder().build());
		return cacheManager;
	}

	Caffeine<Object, Object> caffeineCacheBuilder() {
		return Caffeine.newBuilder()
			.expireAfterWrite(1, TimeUnit.HOURS)
			.maximumSize(100);
	}

	Caffeine<Object, Object> concertSchedulesBuilder() {
		return Caffeine.newBuilder()
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.maximumSize(100);
	}

}
