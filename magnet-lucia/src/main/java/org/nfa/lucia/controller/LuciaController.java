package org.nfa.lucia.controller;

import java.security.Principal;

import org.nfa.athena.AthenaClient;
import org.nfa.athena.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableFeignClients(clients = AthenaClient.class, defaultConfiguration = FeignClientsConfiguration.class)
public class LuciaController {

	private static final Logger LOG = LoggerFactory.getLogger(LuciaController.class);

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private AthenaClient athenaClient;

	@RequestMapping("/welcome")
	public String welcome(Principal principal, @RequestHeader HttpHeaders headers) {
		LOG.info("Headers: {}", headers);
		LOG.info("Welcome {}", principal.getName());
		return "Welcome " + principal.toString();
	}

	@RequestMapping("/athenaUser")
	public User athenaUser(@RequestParam(value = "name", required = false) String name,
			@RequestHeader HttpHeaders headers) {
		System.out.println(discoveryClient.getInstances("magnet-athena").get(0).getUri().toString());
		User user = athenaClient.oneUser();
		// System.out.println("call oneUserByName method: " +
		// athenaController.oneUserByName(name));
		// System.out.println("call oneUserByNamePath method: " +
		// athenaController.oneUserByNamePath(name));
		// greetingController.exception();
		return user;
	}

	@RequestMapping("/oneUserByName")
	public User oneUserByName(@RequestParam(value = "name") String name) {
		return athenaClient.userByName(name);
	}

	@RequestMapping("/oneUserByNamePath")
	public User oneUserByNamePath(@RequestParam(value = "name") String name) {
		return athenaClient.userByNamePath(name);
	}

	// Need to add @RequestLine("GET /users") to the interface
	// GreetingController GreetingController controller =
	// Feign.builder().target(GreetingController.class,
	// "http://localhost:8082/athena");

}
