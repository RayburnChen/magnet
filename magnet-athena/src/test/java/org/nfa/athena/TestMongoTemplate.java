package org.nfa.athena;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MagnetAthenaApplication.class)
public class TestMongoTemplate {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void testScripts() {
		String script = "function(x)    { return x; }";
		Object result = mongoTemplate.scriptOps().execute(new ExecutableMongoScript(script), "directly execute script");
		System.out.println("TestMongoTemplate scriptOps result:" + result.toString());
	}

	@Test
	public void testEnumSet() {
		User user = new User();
		user.setName("peter");
		user.setUserType(UserType.ADMIN);
//		EnumSet<UserType> set = EnumSet.allOf(UserType.class);
		mongoTemplate.insert(user);
		List<User> datas = mongoTemplate.findAll(User.class);
		datas.forEach(u -> {
			System.out.println(u);
		});
	}

}
