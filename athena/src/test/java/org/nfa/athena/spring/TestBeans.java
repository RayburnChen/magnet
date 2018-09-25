package org.nfa.athena.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

@Configuration
public class TestBeans {

	@Bean
	public MongoTemplate mongoTemplate() {
		MongoClient mongo = new MongoClient("localhost", 27017);
		return new MongoTemplate(mongo, "magnet-athena");
	}

}
