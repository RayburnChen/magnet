package org.nfa.athena;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Test;

public class TestCommon {

	@Test
	public void testUUID() {
		System.out.println("TestTryCatch.testUUID " + UUID.randomUUID());
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
		String str1 = new String("Base") + new String("String");// already into pool ?
		String str2 = str1.intern();
		System.out.println(str1 == str2);// why true ?
	}

}
