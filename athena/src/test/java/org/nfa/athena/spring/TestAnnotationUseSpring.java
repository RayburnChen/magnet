package org.nfa.athena.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

public class TestAnnotationUseSpring {

	@Test
	public void test() throws ClassNotFoundException, IOException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
		// 加载系统所有类资源
		Resource[] resources = resourcePatternResolver.getResources("classpath*:org/nfa/*.class");
		List<Class<?>> list = new ArrayList<Class<?>>();
		// 把每一个class文件找出来
		for (Resource r : resources) {
			MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(r);
			Class<?> clazz = TestAnnotationUseSpring.class.getClassLoader()
					.loadClass(metadataReader.getClassMetadata().getClassName());
			list.add(clazz);
		}
		System.out.println("TestAnnotationUseSpring.test()");
		list.forEach(System.out::println);
	}

}
