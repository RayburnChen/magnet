package org.nfa.athena.spring;

import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nfa.athena.MagnetAthenaApplication;
import org.nfa.athena.model.User;
import org.nfa.athena.model.UserType;
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
	
	// server class
	// com.mongodb.connection.DefaultServer
	// connection pool class
	// com.mongodb.connection.DefaultConnectionPool
	// connection class
	// com.mongodb.connection.DefaultConnectionPool.PooledConnection

	// 1. oplog 是幂等的
	
	// 2. secondary拉取primary的oplog
	// secondary先记录某段oplog中的最小时间戳
	// 拉取oplog保存到本地
	// 清空记录的最小时间戳
	// 任何一步失败，截短本地oplog至最小时间戳，从最小时间戳重新拉取
	
	// 3. secondary回放本地的oplog
	// 回放oplog
	// 回放完成，记录oplog最大时间戳
	// 任何一步失败，重放最大时间戳开始的所有oplog
	
	// 4. 保证oplog的顺序
	// 加锁： 生成时间戳，存到_uncommittedRecordIds里面
	// oplog时间戳设好，存入oplog collection，并发，无序
	// 加锁： _uncommittedRecordIds移除该时间戳
	// secondary只能拉取早于_uncommittedRecordIds里的时间戳的数据
	// 保证了一旦时间戳生成，能读取到的oplog必然有序
	
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
	public void testFindOne() {
		System.err.println(
				mongoTemplate.findOne(new Query(Criteria.where("_id").is("5aa892b7aeea821da889ea10")), User.class));
	}

	@Test
	public void testEnumSet() {
		User user = new User();
		user.setName("love");
		user.setUserType(UserType.ADMIN);
		user.setAge(13);
		mongoTemplate.insert(user);
		mongoTemplate
				.find(new Query(Criteria.where("userType").is(UserType.ADMIN)).withHint(NAME_PARTIAL_IDX), User.class)
				.forEach(System.out::println);
	}

	@Test
	public void testAuditCreate() {
		User user = new User();
		user.setName("Name:" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
		user.setUserType(UserType.ADMIN);
		user.setAge(13);
		user.setVersion(1L);// mongoDB will be 0
		user.setCreatedDate(new Date(1423965003862L));// mongoDB will use new date
		mongoTemplate.insert(user);
	}

	@Test
	public void testAuditModify() {
		User user = new User();
		user.setName("Name:" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
		user.setUserType(UserType.ADMIN);
		user.setAge(13);
		user.setVersion(null);// not null will throw OptimisticLockingFailureException
		user.setCreatedDate(new Date(1423965003862L));// mongoDB will use new date when insert
		mongoTemplate.save(user);
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
