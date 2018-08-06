package org.nfa.panel.config;

import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JsonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
		objectMapper.registerModules(new JavaTimeModule(), new Jdk8Module(), new SimpleModule());
		objectMapper.enableDefaultTyping(DefaultTyping.JAVA_LANG_OBJECT, As.PROPERTY);
		objectMapper.setTimeZone(TimeZone.getDefault());
		objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		return objectMapper;
	}

}
