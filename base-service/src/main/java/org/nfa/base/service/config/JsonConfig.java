package org.nfa.base.service.config;

import java.nio.charset.Charset;
import java.util.TimeZone;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JsonConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		return builder -> builder.timeZone(TimeZone.getDefault())
				.modules(new JavaTimeModule(), new Jdk8Module(), new SimpleModule())
				.featuresToEnable(Feature.WRITE_BIGDECIMAL_AS_PLAIN, SerializationFeature.WRITE_DATES_WITH_ZONE_ID,
						SerializationFeature.FAIL_ON_EMPTY_BEANS)
				.featuresToDisable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS,
						SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
						DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.serializationInclusion(Include.NON_NULL);
	}

	@Bean
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper objectMapper = builder.build();
		objectMapper.disableDefaultTyping();
		return objectMapper;
	}

	@Bean
	public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(ObjectMapper objectMapper) {
		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		jackson2HttpMessageConverter.setObjectMapper(objectMapper);
		jackson2HttpMessageConverter.setDefaultCharset(Charset.defaultCharset());
		return jackson2HttpMessageConverter;
	}

}
