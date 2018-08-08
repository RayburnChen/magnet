package org.nfa.lucia.controller;

import org.nfa.athena.model.AthenaClient;
import org.nfa.athena.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

@RestController
@EnableFeignClients(clients = AthenaClient.class, defaultConfiguration = FeignClientsConfiguration.class)
public class LuciaController {

	private static final Logger log = LoggerFactory.getLogger(LuciaController.class);

	@Autowired
	private DiscoveryClient discoveryClient;
	@Autowired
	private AthenaClient athenaClient;
	@Autowired
	private RestOperations restOperations;

	@RequestMapping(method = RequestMethod.GET, value = "/welcome")
	public String welcome(@RequestHeader HttpHeaders headers) {
		log.info("Headers: {}", headers);
		return "Welcome";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/athenaUser")
	public User athenaUser(@RequestHeader HttpHeaders headers) {
		log.info("Athena Instances: " + discoveryClient.getInstances("magnet-athena"));
		return athenaClient.oneUser();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/athenaUser/operations")
	public User athenaUserRestOperations() {
		return restOperations.getForObject("http://magnet-athena/magnet-athena/greeting/oneUser", User.class);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/oneUserByName")
	public User oneUserByName(@RequestParam(value = "name") String name) {
		return athenaClient.userByName(name);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/oneUserByNamePath")
	public User oneUserByNamePath(@RequestParam(value = "name") String name) {
		return athenaClient.userByNamePath(name);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/exception" })
	public User exception() {
		return athenaClient.exception();
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/exception/operations" })
	public User exceptionRestOperations() {
		return restOperations.getForObject("http://magnet-athena/magnet-athena/greeting/exception", User.class);
	}

}
