package org.nfa.athena.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanPostConfig implements BeanPostProcessor {

	private static final Logger log = LoggerFactory.getLogger(BeanPostConfig.class);

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		log.trace("BeanPostConfig process {} before", beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		log.trace("BeanPostConfig process {} after", beanName);
		return bean;
	}

}
