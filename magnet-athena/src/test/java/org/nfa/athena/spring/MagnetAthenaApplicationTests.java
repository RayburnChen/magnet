package org.nfa.athena.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nfa.athena.MagnetAthenaApplication;
import org.nfa.athena.User;
import org.nfa.athena.dao.UserRepository;
import org.nfa.stream.MagnetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MagnetAthenaApplication.class)
public class MagnetAthenaApplicationTests {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MagnetSource magnetSource;

	@Test
	public void contextLoads() {
		User user = new User();
		user.setName("new name");
		userRepository.insert(user);
		userRepository.findAll().forEach(one -> System.err.println(one.getName()));
		System.out.println(userRepository.findOneByName("linda"));
	}
	
	@Test
	public void sendEvent() {
		User user = new User();
		user.setName("one msg");
		user.setPassword("pswd");
		magnetSource.output().send(MessageBuilder.withPayload(user).build());
	}

}
