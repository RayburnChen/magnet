package org.nfa.athena.common;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.nfa.athena.User;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestObjectMapper {

	@Test
	public void testWriteValueAsString() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModules(new JavaTimeModule(), new Jdk8Module(), new SimpleModule());
		objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		User user = new User();
		user.setAge(0);
		user.setName("OOOO");
		user.setId("user001");
		String json = objectMapper.writeValueAsString(user);
		System.out.println(json);
		User newUser = objectMapper.readValue(json, User.class);
		System.out.println(newUser.toString());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReadValue() throws IOException {
		String json = "{\"errors\":[{\"code\":\"SystemError\",\"message\":\"Connection refused (Connection refused) executing GET http://techrefresh-patron-service/patron/8000929\"}]}";
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(json, Map.class);
		System.out.println(map.toString());
	}

}
