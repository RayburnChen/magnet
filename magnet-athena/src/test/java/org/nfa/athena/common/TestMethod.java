package org.nfa.athena.common;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.nfa.athena.AthenaClient;

public class TestMethod {

	@Test
	public void test() {
		Method[] ms = AthenaClient.class.getMethods();
		for (int i = 0; i < ms.length; i++) {
			System.out.println(ms[i].getName() + "  isDefault " + isDefault(ms[i]));
		}
	}
	
	public static boolean isDefault(Method method) {
	    // Default methods are public non-abstract, non-synthetic, and non-static instance methods
	    // declared in an interface.
	    // method.isDefault() is not sufficient for our usage as it does not check
	    // for synthetic methods.  As a result, it picks up overridden methods as well as actual default methods.
	    final int SYNTHETIC = 0x00001000;
	    return ((method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC | SYNTHETIC)) ==
	            Modifier.PUBLIC) && method.getDeclaringClass().isInterface();
	  }

}
