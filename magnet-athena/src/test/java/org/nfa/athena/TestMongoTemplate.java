package org.nfa.athena;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MagnetAthenaApplication.class)
public class TestMongoTemplate {
	
	@Autowired
	private MongoTemplate MongoTemplate;

	@Test
	public void testScripts() {
		String script = "db.getCollection('users').find({})";
		Object result = MongoTemplate.scriptOps().execute(new ExecutableMongoScript(script), "directly execute script");
		System.out.println("TestMongoTemplate.testScripts()" + result.toString());
	}

}
