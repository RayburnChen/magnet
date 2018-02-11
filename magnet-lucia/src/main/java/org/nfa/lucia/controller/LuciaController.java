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

	private static final Logger log = LoggerFactory.getLogger(LuciaController.class);

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private AthenaClient athenaClient;

	@RequestMapping("/welcome")
	public String welcome(Principal principal, @RequestHeader HttpHeaders headers) {
		log.info("Headers: {}", headers);
		log.info("Welcome {}", principal.getName());
		return "Welcome " + principal.toString();
	}

	@RequestMapping("/athenaUser")
	public User athenaUser(@RequestHeader HttpHeaders headers) {
		log.info("Athena Instances: " + discoveryClient.getInstances("magnet-athena"));
		return athenaClient.oneUser();
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
