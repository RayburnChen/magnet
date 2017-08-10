package org.nfa.athena;

import java.util.Map;
import java.util.WeakHashMap;

import org.junit.Test;

public class TestWeakHashMap {

	@Test
	public void test1() {
		String a = new String("a");
		String b = new String("b");
		Map<String, String> weakMap = new WeakHashMap<>();
		weakMap.put(a, "A");
		weakMap.put(b, "B");
		
		// 1
		System.gc();
		System.out.println(weakMap);
		System.out.println(weakMap.size());
		
		// 2
		a = null;
		System.gc();
		System.out.println(weakMap);
		System.out.println(weakMap.size());
		
	}
	
	@Test
	public void test2() {
		String a = new String("a");
		Map<String, String> weakMap = new WeakHashMap<>();
		weakMap.put(a, "A");
		
		// 1
		System.gc();
		System.out.println(weakMap);
		System.out.println(weakMap.size());
		
		processMap(weakMap);
		
		// 3
		System.gc();
		System.out.println(weakMap);
		System.out.println(weakMap.size());
		
	}

	private void processMap(Map<String, String> weakMap) {
		// 2
		String b = new String("B");
		weakMap.put(b, "B");
		System.gc();
		System.out.println(weakMap);
		System.out.println(weakMap.size());
	}
}
