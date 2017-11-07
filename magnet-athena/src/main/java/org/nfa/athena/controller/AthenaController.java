package org.nfa.athena.controller;

import javax.annotation.PostConstruct;

import org.nfa.athena.User;
import org.nfa.athena.dao.UserRepository;
import org.nfa.athena.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping(value = "/greeting")
public class AthenaController implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(AthenaController.class);

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUser" })
	public User oneUser(@RequestHeader HttpHeaders headers) {
		log.info("oneUser");
		log.info("HttpHeaders ", headers);
		AsyncService<User> async = AsyncService.build();
		async.add(() -> userRepository.findAll().get(0));
		return async.getResult().stream().findAny().orElse(null);
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/oneUser" })
	public User insertUser(@RequestBody User user) {
		log.info("insertUser {}", user);
		return userRepository.insert(user);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUserByName" })
	public User oneUserByName(@RequestParam("name") String name) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		log.info(requestAttributes.getRequest().getServletPath());
		log.info("oneUserByName {}", name);
		User user = userRepository.findOneByName(name);
		return user;
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUserByName/{name}" })
	public User oneUserByNamePath(@PathVariable("name") String name) {
		log.info("oneUserByNamePath {}", name);
		User user = userRepository.findOneByName(name);
		return user;
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/exception" })
	public User exception() {
		log.info("User exception {}");
		Assert.notNull(null, "My exception Assert.isNull");
		return null;
	}

	@PostConstruct
	public void init() {
		log.info("@PostConstruct {}", userRepository);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("afterPropertiesSet {}", userRepository);
	}

}
