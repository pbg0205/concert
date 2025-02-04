package com.cooper.concert.storage.cache.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofMinutes(10)) // 기본 TTL 10분
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(
					new GenericJackson2JsonRedisSerializer()));

		final RedisCacheManager redisCacheManager = RedisCacheManager.builder(
				RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
			.cacheDefaults(defaultConfig)
			.withCacheConfiguration("concertScheduleSeat",
				RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(1)))
			.build();

		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("concertSchedules");
		caffeineCacheManager.setCaffeine(caffeineCacheBuilder());
		caffeineCacheManager.registerCustomCache("concertSchedules", concertSchedulesBuilder().build());
		return new CompositeCacheManager(redisCacheManager, caffeineCacheManager);
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
