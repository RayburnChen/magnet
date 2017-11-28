package org.nfa.athena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestStream {

	static Logger LOG = Logger.getLogger(TestStream.class);
	
	@Test
	public void testStream() {
		Random ran = new Random();
		Arrays.asList("A", "B", "C").stream().map(genUnary(ran.nextInt())).forEach(genCons(ran.nextInt()));
		Arrays.asList("D", "E", "F").forEach(genCons(ran.nextInt()));
	}

	private UnaryOperator<String> genUnary(int n) {
		return s -> {
			System.out.println(s + "-" + n);
			return s;
		};
	}

	private Consumer<String> genCons(int n) {
		return s -> {
			System.out.println(s + "-" + n);
		};
	}

	@Test
	public void test() {
		int size = 1000000;
		long forMethod = 0L;
		long streamMethod = 0L;
		for (int i = 0; i < 30; i++) {
			List<Integer> data = initial(size);
			forMethod = forMethod + forMethod(data);
		}
		for (int i = 0; i < 30; i++) {
			List<Integer> data = initial(size);
			streamMethod = streamMethod + streamMethod(data);
		}
		LOG.info(" forMethod total is " + forMethod);
		LOG.info(" streamMethod total is " + streamMethod);
	}

	private List<Integer> initial(int size) {
		Random random = new Random();
		List<Integer> list = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			list.add(random.nextInt());
		}
		return list;
	}

	private long forMethod(List<Integer> list) {
		long t = System.currentTimeMillis();
		for (Integer one : list) {
			System.err.println(one);
		}
		long n = System.currentTimeMillis();
		LOG.info("forMethod cost" + String.valueOf(n - t));
		return n - t;
	}

	private long streamMethod(List<Integer> list) {
		long t = System.currentTimeMillis();
		list.stream().forEach(one -> System.err.println(one));
		long n = System.currentTimeMillis();
		LOG.info("streamMethod cost " + String.valueOf(n - t));
		return n - t;
	}

}
