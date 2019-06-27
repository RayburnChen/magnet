package org.nfa.athena.repository;

import org.nfa.athena.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	
	public User findOneByName(String name);

}
