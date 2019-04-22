package org.nfa.base.service.config;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class RedisConfig {

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, CacheProperties cacheProperties,
			GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
		RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory)
				.cacheDefaults(determineConfiguration(cacheProperties, genericJackson2JsonRedisSerializer));
		List<String> cacheNames = cacheProperties.getCacheNames();
		if (!cacheNames.isEmpty()) {
			builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
		}
		return builder.build();
	}

	@Bean
	public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper objectMapper = builder.build();
		objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		return new GenericJackson2JsonRedisSerializer(objectMapper);
	}

	@Bean
	public RedisOperations<String, Object> redisOperations(RedisConnectionFactory connectionFactory,
			GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(StandardCharsets.UTF_8);
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setEnableDefaultSerializer(true);
		redisTemplate.setDefaultSerializer(genericJackson2JsonRedisSerializer);
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
		redisTemplate.setStringSerializer(stringRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	private RedisCacheConfiguration determineConfiguration(CacheProperties cacheProperties,
			GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
		Redis redisProperties = cacheProperties.getRedis();
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
		config = config.serializeValuesWith(SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer));
		if (redisProperties.getTimeToLive() != null) {
			config = config.entryTtl(redisProperties.getTimeToLive());
		}
		if (redisProperties.getKeyPrefix() != null) {
			config = config.prefixKeysWith(redisProperties.getKeyPrefix());
		}
		if (!redisProperties.isCacheNullValues()) {
			config = config.disableCachingNullValues();
		}
		if (!redisProperties.isUseKeyPrefix()) {
			config = config.disableKeyPrefix();
		}
		return config;
	}

}
