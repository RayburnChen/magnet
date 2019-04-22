package org.nfa.athena.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;

@Service
public class MongoObjectReader extends AbstractObjectReader {

	private static final Logger log = LoggerFactory.getLogger(MongoObjectReader.class);

	public <S extends Object> S read(Class<S> clazz, final DBObject dbo) {
		log.info("MongoObjectReader Class {}, DBObject {}", clazz, dbo);
		return null;
	}

	public <S extends Object> S read(Class<S> clazz, final String json) {
		return read(clazz, parse(json));
	}

}
