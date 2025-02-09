package com.cooper.concert.storage.cache.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class LocalCacheConfig {

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("concertSchedules");
		caffeineCacheManager.setCaffeine(caffeineCacheBuilder());
		caffeineCacheManager.registerCustomCache("concertSchedules", concertSchedulesBuilder().build());
		return caffeineCacheManager;
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
