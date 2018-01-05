package org.nfa.athena;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MagnetAthenaApplication.class)
public class TestRedisTemplate {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	public void test() {
		stringRedisTemplate.opsForValue().set("001", "user");
		System.err.println("StringRedisTemplate opsForValue " + stringRedisTemplate.opsForValue().get("001"));
		System.err.println("StringRedisTemplate opsForValue " + stringRedisTemplate.opsForValue().get("002"));
	}

}
