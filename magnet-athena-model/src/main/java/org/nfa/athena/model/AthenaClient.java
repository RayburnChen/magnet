package org.nfa.athena.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * see org.springframework.cloud.netflix.feign.FeignAutoConfiguration
 *
 */
@FeignClient(name = "magnet-athena", path = "magnet-athena", configuration = FeignClientsConfiguration.class)
@RequestMapping(value = "/greeting")
public interface AthenaClient {

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUser" })
	public User oneUser();

	@RequestMapping(method = RequestMethod.GET, value = { "/user" })
	public User user(@RequestParam("id") String id);

	@RequestMapping(method = RequestMethod.POST, value = { "/user" })
	public User insertUser(@RequestBody User user);

	@RequestMapping(method = RequestMethod.GET, value = { "/userByName" })
	public User userByName(@RequestParam("name") String name);

	@RequestMapping(method = RequestMethod.GET, value = { "/userByName/{name}" })
	public User userByNamePath(@PathVariable("name") String name);

	@RequestMapping(method = RequestMethod.GET, value = { "/exception" })
	public User exception();

}
