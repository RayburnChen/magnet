package org.nfa.athena;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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

	@Test
	public void testListAdd() {
		User user = new User();
		user.setValues(new ArrayList<>());
		List<String> first = Arrays.asList("first");
		List<String> second = new LinkedList<>();
		second.add("second");
		user.getValues().addAll(first);
		user.getValues().addAll(second);
		System.out.println(user);
	}

	@Test
	public void testBigDecimal() {
		String aa = null;
		BigDecimal big = new BigDecimal(aa);
		System.out.println("TestTryCatch.testBigDecimal()" + big.toPlainString());
	}

	@Test
	public void testString() {
		String base = "Base";
		final String baseFinal = "Base";

		String str1 = "BaseString";
		String str2 = "Base" + "String";
		String str3 = base + "String";
		String str4 = baseFinal + "String";
		String str5 = new String("BaseString").intern();
		String str6 = new String("Base") + new String("String");

		System.out.println(str1 == str2);// true
		System.out.println(str1 == str3);// false
		System.out.println(str1 == str4);// true
		System.out.println(str1 == str5);// true
		System.out.println(str1 == str6);// false
	}

	@Test
	public void testStringPlus() {
		String str1 = new String("Base") + new String("String");// already into poll ?
		String str2 = str1.intern();
		System.out.println(str1 == str2);// why true ?
	}

}
