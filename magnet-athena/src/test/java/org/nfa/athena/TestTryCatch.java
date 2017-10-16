package org.nfa.athena;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.WeakHashMap;

import org.junit.Test;

public class TestTryCatch {

	@Test
	public void test() {
		System.out.println(testTryCatch());
	}

	@SuppressWarnings("finally")
	private String testTryCatch() {
		String a = new String("a");
		// System.out.println(a);
		WeakReference<String> b = new WeakReference<String>(a);
		// System.out.println(b.get());
		WeakHashMap<String, Integer> weakMap = new WeakHashMap<String, Integer>();
		weakMap.put(b.get(), 1);
		a = null;
		System.out.println("GC前b.get()：" + b.get());
		System.out.println("GC前weakMap：" + weakMap);
		System.gc();
		System.out.println("GC后" + b.get());
		System.out.println("GC后" + weakMap);
		String c = "";
		try {
			c = b.get().replace("a", "b");
			System.out.println("C:" + c);
			return c;// never return
		} catch (Exception e) {
			c = "c";
			System.out.println("Exception");
			return c + "f";
		} finally {
			c += "d";
			return c + "e";
		}
	}
	
	@Test
	public void testUUID() {
		System.out.println("TestTryCatch.testUUID " + UUID.randomUUID());
	}

}
