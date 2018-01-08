package org.nfa.athena;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MagnetAthenaApplication.class)
public class TestRedisTemplate {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	public void opsForValue() {
		stringRedisTemplate.opsForValue().set("key01", "user01");
		System.err.println("StringRedisTemplate opsForValue " + stringRedisTemplate.opsForValue().get("001"));
		System.err.println("StringRedisTemplate opsForValue " + stringRedisTemplate.opsForValue().get("002"));
	}

	@Test
	public void opsForList() {
		stringRedisTemplate.opsForList().rightPush("list01", "user01");
		long end = stringRedisTemplate.opsForList().size("list01");
		List<String> list = stringRedisTemplate.opsForList().range("list01", 0, end);
		System.err.println(list);
	}

	@Test
	public void boundHashOps() {
		stringRedisTemplate.boundHashOps("hash01").put("k01", "v01");
		stringRedisTemplate.boundHashOps("hash01").put("k02", "v02");
		stringRedisTemplate.boundHashOps("hash01").put("k03", "v03");
		System.err.println(stringRedisTemplate.boundHashOps("hash01").entries());
	}

	@Test
	public void opsForHash() {
		stringRedisTemplate.opsForHash().put("hash02", "k01", "v01");
		stringRedisTemplate.opsForHash().put("hash02", "k02", "v02");
		stringRedisTemplate.opsForHash().put("hash02", "k03", "v03");
		System.err.println(stringRedisTemplate.opsForHash().entries("hash02"));
	}

	@Test
	public void execute() {
		// execute a transaction
		List<Object> txResults = stringRedisTemplate.execute(new SessionCallback<List<Object>>() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public List<Object> execute(RedisOperations operations) throws DataAccessException {
				operations.multi();
				operations.opsForSet().add("set01", "obj01", "obj02", "obj03");

				// This will contain the results of all ops in the transaction
				return operations.exec();
			}

		});
		System.err.println("Number of items added to set Results : " + txResults);
	}

}
