package org.nfa.panel.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nfa.panel.service.AuditEventRepositoryImpl;
import org.nfa.panel.service.TraceRepositoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableSwagger2
@EnableMongoAuditing
@EnableMongoRepositories
@EnableScheduling
@EnableResourceServer
@Import(value = { JsonConfig.class, GlobalErrorController.class, GlobalExceptionHandler.class, WebMvcConfigurerInitializer.class, HttpClientConfig.class, ThreadPoolConfigurer.class,
		AuditEventRepositoryImpl.class, TraceRepositoryImpl.class })
public @interface EnableMagnetService {

	String value() default "";

	String comments() default "";

}
