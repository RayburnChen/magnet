package org.nfa.athena.config;

import java.util.Set;

import org.nfa.athena.repository.ProxyInterface;
import org.nfa.athena.repository.ScannerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

@Configuration
public class ClassScanner implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(ClassScanner.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false); // 不使用默认的TypeFilter
//		provider.addIncludeFilter(new AnnotationTypeFilter(Service.class));
		provider.addIncludeFilter(new AssignableTypeFilter(ScannerInterface.class));
		provider.addIncludeFilter(new AssignableTypeFilter(ProxyInterface.class));
		Set<BeanDefinition> beanDefinitionSet = provider.findCandidateComponents("org.nfa.athena");
		beanDefinitionSet
				.forEach(beanDefinition -> log.info("ClassScanner find bean {}", beanDefinition.getBeanClassName()));
	}

}
