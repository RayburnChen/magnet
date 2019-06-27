package org.nfa.athena.service;

import java.util.Optional;

import org.nfa.athena.config.CacheConfig;
import org.nfa.athena.model.User;
import org.nfa.athena.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AthenaService {

	@Autowired
	private UserRepository userRepository;

	public User oneUser() {
		return Optional.of(userRepository.findAll()).filter(l -> !l.isEmpty()).map(l -> l.get(0)).orElse(new User());
	}

	public User insert(User user) {
		return userRepository.insert(user);
	}

	@Cacheable(CacheConfig.USER_CACHE)
	public User findOne(String id) {
		return userRepository.findById(id).orElse(null);
	}

	public User findOneByName(String name) {
		return userRepository.findOneByName(name);
	}

}
