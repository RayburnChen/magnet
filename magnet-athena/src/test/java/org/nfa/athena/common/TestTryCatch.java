package org.nfa.athena.common;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.beanutils.BeanUtils;
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
	public void testBeanUtils() {

		// no exception
		Map<String, Object> bean2 = new HashMap<>();
		bean2.put("aaa", new HashMap<>());
		try {
			System.out.println(BeanUtils.getNestedProperty(bean2, "aaa.bb"));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}

		// NestedNullException
		Map<String, Object> bean1 = new HashMap<>();
		try {
			System.out.println(BeanUtils.getNestedProperty(bean1, "aaa.bb"));
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}

	}

}
