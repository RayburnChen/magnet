package org.nfa.athena.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.nfa.athena.dao.ProxyInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanFactoryPostConfig implements BeanFactoryPostProcessor {

	private static final Logger log = LoggerFactory.getLogger(BeanFactoryPostConfig.class);

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
//		org.springframework.beans.factory.support.DefaultListableBeanFactory
		beanFactory.registerSingleton("myProxyInterface", Proxy.newProxyInstance(this.getClass().getClassLoader(),
				new Class[] { ProxyInterface.class }, new InvocationHandler() {

					private ProxyInterface target = () -> log.info("代理类target");

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return method.invoke(target, args);
					}
				}));
		log.info("调用了自定义的BeanFactoryPostProcessor ");
		String[] names = beanFactory.getBeanDefinitionNames();
		// 获取了所有的bean名称列表
		for (int i = 0; i < names.length; i++) {
			String name = names[i];

			BeanDefinition bd = beanFactory.getBeanDefinition(name);
			log.trace("{} bean properties: {}", name, bd.getPropertyValues());
			// 本内容只是个demo，打印持有的bean的属性情况
		}
	}

}
