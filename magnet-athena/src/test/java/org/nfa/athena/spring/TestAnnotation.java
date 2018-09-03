package org.nfa.athena.spring;

import java.util.ServiceLoader;

import org.junit.Test;
import org.nfa.panel.config.EnableMagnetService;

import com.fasterxml.jackson.databind.Module;

public class TestAnnotation {

	@Test
	public void test() throws ClassNotFoundException {

		Class<?> clazz = TestAnnotation.class.getClassLoader().loadClass("org.nfa.athena.service.AsyncService");
		System.out.println("Load " + clazz);
		if (clazz.isAnnotationPresent(EnableMagnetService.class)) {
			EnableMagnetService anno = clazz.getAnnotation(EnableMagnetService.class);
			System.out.println("@EnableMagnetService comments " + anno.comments());
			System.out.println("@EnableMagnetService value " + anno.value());
		}

	}

	@Test
	public void testServiceLoader() {
		// load class through configuration files
		// META-INF/services/configuration
		// e.g com.fasterxml.jackson.databind.module.SimpleModule
		// e.g com.fasterxml.jackson.module.afterburner.AfterburnerModule
		ServiceLoader.load(Module.class, ClassLoader.getSystemClassLoader()).forEach(m -> System.out.println(m));
	}

}
