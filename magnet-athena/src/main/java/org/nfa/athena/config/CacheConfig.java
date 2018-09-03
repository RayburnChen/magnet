package org.nfa.athena.config;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig {

	public static final String USER_CACHE = "USER_CACHE";
	public static final String USER_NAME_CACHE = "USER_NAME_CACHE";

	@Autowired
	private CacheProperties cacheProperties;

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		// configure and return an implementation of Spring's CacheManager SPI
		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(cacheProperties.getRedis().getTimeToLive()).disableCachingNullValues()
				.computePrefixWith(cacheName -> cacheProperties.getRedis().getKeyPrefix() + cacheName + ":")
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
		return RedisCacheManager.builder(connectionFactory).cacheDefaults(defaultCacheConfig).transactionAware().build();
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModules(new JavaTimeModule(), new Jdk8Module(), new SimpleModule());
		mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		mapper.setTimeZone(TimeZone.getDefault());
		RedisSerializer<Object> stringSerializer = new GenericJackson2JsonRedisSerializer(mapper);
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setHashValueSerializer(stringSerializer);
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

}
