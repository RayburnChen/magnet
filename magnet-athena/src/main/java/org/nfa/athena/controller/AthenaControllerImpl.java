package org.nfa.athena.controller;

import org.nfa.athena.User;
import org.nfa.athena.dao.UserRepository;
import org.nfa.athena.service.CallAsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/greeting")
public class AthenaControllerImpl {
	
	private static Logger log = LoggerFactory.getLogger(AthenaControllerImpl.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CallAsyncService callAsyncService;

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUser" })
	public User oneUser() {
		log.info("oneUser");
		callAsyncService.asyncMethod();
		log.info("AthenaControllerImpl.oneUser()");
		return userRepository.findAll().get(0);
	}
	
	

	@RequestMapping(method = RequestMethod.POST, value = { "/oneUser" })
	public User oneUserByName(@RequestParam("name") String name) {
		log.info("oneUserByName {}", name);
		User user = userRepository.findOneByName(name);
		return user;
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUserByName" })
	public User oneUserByNamePath(@PathVariable("name") String name) {
		log.info("oneUserByNamePath {}", name);
		User user = userRepository.findOneByName(name);
		return user;
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUserByName/{name}" })
	public User insertUser(@RequestBody User user) {
		log.info("insertUser {}", user);
		return userRepository.insert(user);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/exception" })
	public User exception() {
		log.info("User exception {}");
		Assert.notNull(null, "My exception Assert.isNull");
		return null;
	}
	
}
