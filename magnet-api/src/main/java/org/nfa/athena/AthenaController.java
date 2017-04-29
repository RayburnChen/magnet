package org.nfa.athena;

import org.nfa.athena.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@FeignClient(name = "magnet-athena", configuration = FeignClientsConfiguration.class)
@RestController
@RequestMapping(value = "/greeting", produces = "application/json; charset=utf-8")
public interface AthenaController {

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUser" })
	public User oneUser();

	@RequestMapping(method = RequestMethod.POST, value = { "/oneUser" })
	public User insertUser(@RequestBody User user);

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUserByName" })
	public User oneUserByName(@RequestParam("name") String name);

	@RequestMapping(method = RequestMethod.GET, value = { "/oneUserByName/{name}" })
	public User oneUserByNamePath(@PathVariable("name") String name);
	
	@RequestMapping(method = RequestMethod.GET, value = { "/exception" })
	public User exception();

}
