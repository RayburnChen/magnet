package org.nfa.athena.dao;

import org.nfa.athena.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	
	public User findOneByName(String name);

}
