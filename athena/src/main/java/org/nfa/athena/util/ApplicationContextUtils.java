package org.nfa.athena.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Starting Servlet Engine: Apache Tomcat
 * Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext
 * Fetching config
 * Refreshing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext
 * Initializing Spring embedded WebApplicationContext
 * Root WebApplicationContext: initialization completed
 * Beans start to load
 * 1. @PostConstruct
 * 2. InitializingBean.afterPropertiesSet()
 * 3. ApplicationContextAware
 * 4. Registering Other Beans
 * 5. ApplicationListener
 * Tomcat started
 * Started MagnetAthenaApplication
 * 
 * @author OwenChen
 *
 */
@Service
public class ApplicationContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	private ApplicationContextUtils() {
		super();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext
		ApplicationContextUtils.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

}
