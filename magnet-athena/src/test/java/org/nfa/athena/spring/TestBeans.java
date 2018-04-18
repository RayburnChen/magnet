package org.nfa.athena.spring;

import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

@Configuration
public class TestBeans {

	@Bean
	public MongoTemplate mongoTemplate() {
		MongoClient mongo = new MongoClient("localhost", 27017);
		return new MongoTemplate(mongo, "magnet-athena");
	}

	@Bean
	public AuditorAware<Date> auditorAware() {
		return () -> new Date(1423965003862L);
	}

}
