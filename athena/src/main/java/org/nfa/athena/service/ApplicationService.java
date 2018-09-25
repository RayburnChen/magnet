package org.nfa.athena.service;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class ApplicationService implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("ApplicationService onApplicationEvent ContextRefreshedEvent" + event.getSource().getClass());
		// org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext
		Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(RestController.class);
		// {athenaController : org.nfa.athena.controller.AthenaController}
		beans.forEach((k, v) -> {
			log.info("ApplicationService name:{}, value:{}", k, v);
			for (Method m : v.getClass().getDeclaredMethods()) {
				RequestMapping anno = m.getAnnotation(RequestMapping.class);
				if (null != anno) {
					log.info("ApplicationService {} Method:{}, Path:{}", k, anno.method(), anno.value());
				}
			}
		});
	}

}
