package org.nfa.athena;

import org.junit.Test;

public class TestAnnotation {

	@Test
	public void test() throws ClassNotFoundException {
		
		Class<?> clazz = TestAnnotation.class.getClassLoader().loadClass("org.nfa.athena.service.AsyncService");
		System.out.println("Load " + clazz);
		if (clazz.isAnnotationPresent(EnableMagnetService.class)) {
			EnableMagnetService anno = clazz.getAnnotation(EnableMagnetService.class);
			System.out.println("@EnableMagnetService comments " + anno.comments());
			System.out.println("@EnableMagnetService priority " + anno.priority());
			System.out.println("@EnableMagnetService value " + anno.value());
		}

	}

}
