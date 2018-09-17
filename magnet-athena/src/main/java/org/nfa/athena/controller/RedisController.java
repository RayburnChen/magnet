package org.nfa.athena.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/operation")
public class RedisController {

	@Autowired(required = false)
	private RedisTemplate<String, Object> redisTemplate;

	@RequestMapping(method = RequestMethod.GET, value = { "/redis/keys" })
	public Set<String> keys(@RequestParam String prefix) {
		return redisTemplate.keys(prefix);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/redis" })
	public List<Object> multiGet(@RequestParam String prefix) {
		return redisTemplate.opsForValue().multiGet(redisTemplate.keys(prefix));
	}

	@RequestMapping(method = RequestMethod.DELETE, value = { "/redis" })
	public Long delete(@RequestParam String prefix) {
		return redisTemplate.delete(redisTemplate.keys(prefix));
	}

}
