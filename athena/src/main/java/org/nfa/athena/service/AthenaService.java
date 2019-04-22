package org.nfa.athena.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.nfa.athena.config.CacheConfig;
import org.nfa.athena.dao.UserRepository;
import org.nfa.athena.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

@Service
public class AthenaService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RedisOperations<String, Object> redisOperations;

	public User oneUser() {
		return Optional.of(userRepository.findAll()).filter(l -> !l.isEmpty()).map(l -> l.get(0)).orElse(new User());
	}

	public User insert(User user) {
		return userRepository.insert(user);
	}

	@Cacheable(CacheConfig.USER_CACHE)
	public User findOne(String id) {
		User user = userRepository.findById(id).orElse(null);
		if (null != user) {
			redisOperations.opsForValue().set(user.getId(), user, 60L, TimeUnit.MINUTES);
		}
		return user;
	}

	@Cacheable(CacheConfig.USER_NAME_CACHE)
	public User findOneByName(String name) {
		User user = userRepository.findOneByName(name);
		if (null != user) {
			redisOperations.opsForValue().set(user.getId(), user, 60L, TimeUnit.MINUTES);
		}
		return user;
	}

}
