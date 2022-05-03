package org.nfa.athena.common;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.nfa.athena.model.User;
import org.springframework.util.StopWatch;

public class TestCommon {

	@Test
	public void testUUID() {
		System.out.println("TestTryCatch.testUUID " + UUID.randomUUID());
	}

	@Test
	public void addAllList() {
		List<String> arrayList = Arrays.asList("arrayList");
		List<String> linkedList = new LinkedList<>();
		linkedList.add("linkedList");
		arrayList.addAll(linkedList);// UnsupportedOperationException
	}

	@Test
	public void testBigDecimal() {
		System.out.println(new BigDecimal(0.01));
		System.out.println(BigDecimal.valueOf(0.01));
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
		String str1 = new String("Base") + new String("String");// already into pool ?
		String str2 = str1.intern();
		System.out.println(str1 == str2);// why true ?
	}

	@Test
	public void testInetAddress() throws UnknownHostException {
		InetAddress add = InetAddress.getLocalHost();
		System.out.println(add.getHostName());
		System.out.println(add.getHostAddress());
	}

	@Test
	public void testStopWatch() throws InterruptedException {
		StopWatch sw = new StopWatch();
		sw.start("testStopWatch01");
		TimeUnit.SECONDS.sleep(1L);
		sw.stop();
		sw.start("testStopWatch02");
		TimeUnit.SECONDS.sleep(2L);
		sw.stop();
		System.out.println(sw.prettyPrint());
	}

	@Test
	public void testIfElseShorthand() {
		User user = new User();
		String str = (user.getName() != null) ? user.getName().toLowerCase(Locale.ROOT) : nested();
		System.out.println(str);
	}

	private String nested() {
		throw new RuntimeException("nested");
	}

}
