package org.nfa.athena.controller;

import javax.validation.constraints.Max;

import org.nfa.athena.model.User;
import org.nfa.athena.service.AthenaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

import brave.Tracer;

@RestController
@RequestMapping(value = "/greeting")
@Validated
public class AthenaController {

	private static final Logger log = LoggerFactory.getLogger(AthenaController.class);

	@Autowired
	private AthenaService athenaService;
	@Autowired
	private Tracer tracer;

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUser" })
	public User oneUser(@RequestHeader HttpHeaders headers) throws JsonProcessingException {
		log.info("HttpHeaders ", headers);
		log.info(tracer.currentSpan().context().toString());
		return athenaService.oneUser();
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/user" })
	public User user(@RequestParam(value = "id") String id) {
		return athenaService.findOne(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/user" })
	public User insertUser(@RequestBody @Validated User user) {
		log.info("insertUser {}", user);
		return athenaService.insert(user);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/userByName" })
	public User userByName(@RequestParam("name") String name, @RequestParam(value = "age", required = false) @Max(10) Integer age) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		log.info(requestAttributes.getRequest().getServletPath());
		log.info("oneUserByName {}", name);
		return athenaService.findOneByName(name);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/userByName/{name}" })
	public User userByNamePath(@PathVariable("name") String name) {
		log.info("oneUserByNamePath {}", name);
		return athenaService.findOneByName(name);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/exception" })
	public User exception() {
		log.info("User exception {}");
		throw new RuntimeException("My exception Assert.isNull");
	}

}
