package org.nfa.athena.controller;

import java.lang.management.ManagementFactory;

import org.nfa.athena.AthenaController;
import org.nfa.athena.User;
import org.nfa.athena.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
@Service
public class AthenaControllerImpl implements AthenaController {
	
	private static Logger log = LoggerFactory.getLogger(AthenaControllerImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public User oneUser() {
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();
		log.info("getFreePhysicalMemorySize {}", osmxb.getFreePhysicalMemorySize());
		log.info("oneUser");
		return userRepository.findAll().get(0);
	}

	@Override
	public User oneUserByName(@RequestParam("name") String name) {
		log.info("oneUserByName {}", name);
		User user = userRepository.findOneByName(name);
		return user;
	}

	@Override
	public User oneUserByNamePath(@PathVariable("name") String name) {
		log.info("oneUserByNamePath {}", name);
		User user = userRepository.findOneByName(name);
		return user;
	}

	@Override
	public User insertUser(@RequestBody User user) {
		log.info("insertUser {}", user);
		return userRepository.insert(user);
	}

	@Override
	public User exception() {
		log.info("User exception {}");
		Assert.notNull(null, "My exception Assert.isNull");
		return null;
	}
	
}
