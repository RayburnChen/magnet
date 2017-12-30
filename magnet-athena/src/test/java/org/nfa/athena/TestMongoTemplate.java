package org.nfa.athena;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.PartialIndexFilter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MagnetAthenaApplication.class)
public class TestMongoTemplate {

	private final static String NAME_PARTIAL_IDX = "name_partial_idx";
	private final static Criteria PARTIAL_CT = Criteria.where("userType").is(UserType.ADMIN);

	@Autowired
	private MongoTemplate mongoTemplate;

	@PostConstruct
	public void init() {
		IndexDefinition idx = new Index("name", Direction.ASC).on("age", Direction.ASC)
				.partial(PartialIndexFilter.of(PARTIAL_CT)).named(NAME_PARTIAL_IDX).background();
		mongoTemplate.indexOps(User.class).ensureIndex(idx);
	}

	@Test
	public void testScripts() {
		String script = "function(x)    { return x; }";
		Object result = mongoTemplate.scriptOps().execute(new ExecutableMongoScript(script), "directly execute script");
		System.out.println("TestMongoTemplate scriptOps result:" + result.toString());
	}

	@Test
	public void testClone() {
		User user = new User();
		user.setName("apple");
		user.setUserType(UserType.ADMIN);
		mongoTemplate.insert(user);
		String script = "function(x)    { u = db.users.findOne({'name':'apple'}); u._id = x; r = db.users.insert(u); return r.nInserted; }";
		Object result = mongoTemplate.scriptOps().execute(new ExecutableMongoScript(script), "new-id-002");
		Double n = Double.valueOf(String.valueOf(result));
		System.err.println(n == 0.0);
	}

	@Test
	public void testEnumSet() {
		User user = new User();
		user.setName("love");
		user.setUserType(UserType.ADMIN);
		user.setAge(13);
		mongoTemplate.insert(user);
		mongoTemplate.find(new Query(PARTIAL_CT).withHint(NAME_PARTIAL_IDX), User.class).forEach(System.out::println);
	}

	@Test
	public void testProject() {
		Query q = new Query();
		projectFields(q.fields(), User.class);
		mongoTemplate.find(q, User.class).forEach(System.out::println);
	}

	private <V> void projectFields(org.springframework.data.mongodb.core.query.Field fs, Class<V> clazz) {
		classIterator(fs, clazz);
	}

	private <V> void classIterator(org.springframework.data.mongodb.core.query.Field fs, Class<V> clazz) {
		Arrays.stream(clazz.getDeclaredFields())
				.filter(f -> !f.isAnnotationPresent(Transient.class) && !Modifier.isStatic(f.getModifiers()))
				.map(f -> f.getName()).forEach(f -> fs.include(f));
		if (null != clazz.getSuperclass()) {
			classIterator(fs, clazz.getSuperclass());
		}
	}

}
