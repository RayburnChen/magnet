package org.nfa.panel.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableDiscoveryClient
@EnableSwagger2
@EnableMongoAuditing
@Import(value = { JsonConfig.class, GlobalErrorController.class, GlobalExceptionHandler.class, WebMvcConfigurerInitializer.class, FeignClientConfig.class, RestTemplateConfig.class })
public @interface EnableMagnetService {

	String value() default "";

	String comments() default "";

}
