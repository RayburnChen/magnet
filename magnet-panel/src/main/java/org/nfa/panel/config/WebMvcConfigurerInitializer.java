package org.nfa.panel.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebMvcConfigurerInitializer extends WebMvcConfigurerAdapter {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		jackson2HttpMessageConverter.setObjectMapper(objectMapper);
		converters.add(jackson2HttpMessageConverter);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RequestLogger());
	}

}
