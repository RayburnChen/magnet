package org.nfa.athena.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.junit.Test;

public class TestStream {

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
		System.out.println(" forMethod total is " + forMethod);
		System.out.println(" streamMethod total is " + streamMethod);
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
		System.out.println("forMethod cost" + String.valueOf(n - t));
		return n - t;
	}

	private long streamMethod(List<Integer> list) {
		long t = System.currentTimeMillis();
		list.stream().forEach(one -> System.err.println(one));
		long n = System.currentTimeMillis();
		System.out.println("streamMethod cost " + String.valueOf(n - t));
		return n - t;
	}

	@Test
	public void testSpliterator() {
		List<Integer> arrayList = new ArrayList<>();
		System.out.println(arrayList.spliterator().characteristics());
		List<Integer> linkedList = new LinkedList<>();
		System.out.println(linkedList.spliterator().characteristics());
	}

}
