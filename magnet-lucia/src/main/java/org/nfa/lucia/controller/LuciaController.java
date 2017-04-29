package org.nfa.lucia.controller;

import org.nfa.athena.AthenaController;
import org.nfa.athena.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LuciaController {
	
	private static Logger log = LoggerFactory.getLogger(LuciaController.class);

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private AthenaController greetingController;

	@RequestMapping(method = RequestMethod.GET, value = { "", "/" }, produces = "text/html; charset=utf-8")
	public String welcome(@RequestHeader HttpHeaders headers) {
		log.info("welcome");
		return "welcome";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/athenaUser", produces = "application/json; charset=utf-8")
	public User athenaUser(@RequestParam(value = "name", required = false) String name, @RequestHeader HttpHeaders headers) {
		System.out.println(discoveryClient.getInstances("magnet-athena").get(0).getUri().toString());
		greetingController.oneUser();
		System.out.println("call oneUserByName method: " + greetingController.oneUserByName(name));
		System.out.println("call oneUserByNamePath method: " + greetingController.oneUserByNamePath(name));
//		greetingController.exception();
		return greetingController.oneUserByName(name);
	}

	// Need to add @RequestLine("GET /users") to the interface
	// GreetingController GreetingController controller = Feign.builder().target(GreetingController.class, "http://localhost:8082/athena");

}
