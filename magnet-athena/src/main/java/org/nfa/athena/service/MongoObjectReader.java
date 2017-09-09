package org.nfa.athena.service;

import org.springframework.stereotype.Service;

import com.mongodb.DBObject;

@Service
public class MongoObjectReader extends AbstractObjectReader {

	public <S extends Object> S read(Class<S> clazz, final DBObject dbo) {
		return null;
	}

	public <S extends Object> S read(Class<S> clazz, final String json) {
		return read(clazz, parse(json));
	}

}
