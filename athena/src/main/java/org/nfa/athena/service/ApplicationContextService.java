package org.nfa.athena.service;

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
public class ApplicationContextService implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	private ApplicationContextService() {
		super();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		// org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext
		this.applicationContext = applicationContext;
	}

	public Object getBean(String name) {
		return applicationContext.getBean(name);
	}

}
