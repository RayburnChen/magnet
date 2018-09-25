package org.nfa.athena.common;

import org.junit.Test;

public class TestInteger {

	@Test
	public void test() {
		// [-128, 127] use cache
		printEquals(127, 127);
		printEquals(128, 128);
		printEquals(-128, -128);
		printEquals(-129, -129);
	}

	private void printEquals(Integer a, Integer b) {
		System.out.println(a + " == " + b + " is " + String.valueOf(a == b));
	}

	// 127 == 127 is true
	// 128 == 128 is false
	// -128 == -128 is true
	// -129 == -129 is false

}
