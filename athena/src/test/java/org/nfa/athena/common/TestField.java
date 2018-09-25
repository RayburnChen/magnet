package org.nfa.athena.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.nfa.athena.model.User;
import org.springframework.data.annotation.Transient;

public class TestField {

	private List<String> list = Arrays.asList("a", "aa", "aaa", "b", "bb", "bbb", "c", "cc", "ccc");

	@Test
	public void test() {
		Map<Integer, String> map = list.stream()
				.collect(Collectors.groupingBy(String::length, Collectors.reducing("", (x, y) -> y)));
		System.out.println(map.values());
	}
	
	@Test
	public void testFields() {
		Field[] dfs = User.class.getDeclaredFields();
		Field[] fs = User.class.getFields();
		for (int i = 0; i < dfs.length; i++) {
			System.out.println("getDeclaredFields" + dfs[i].getName());
			System.out.println("isAnnotationPresent" + dfs[i].isAnnotationPresent(Transient.class));
		}
		for (int i = 0; i < fs.length; i++) {
			System.out.println("getFields" + fs[i].getName());
			System.out.println("isAnnotationPresent" + fs[i].isAnnotationPresent(Transient.class));
		}
	}
	
	@Test
	public void testFields1() {
		System.out.println(1 == 3);
	}

}
