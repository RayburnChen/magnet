package org.nfa.zuul;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Configuration
@Primary
@ConfigurationProperties(prefix = "zuul")
public class SwaggerConfig implements SwaggerResourcesProvider {

	private Map<String, Map<String, String>> routes = new HashMap<>();

	private String ignoredPatterns;

	@Override
	public List<SwaggerResource> get() {
		return Optional.ofNullable(routes).orElse(Collections.emptyMap()).entrySet().stream().map(entry -> {
			SwaggerResource swaggerResource = new SwaggerResource();
			swaggerResource.setName(entry.getKey());
			swaggerResource.setLocation(entry.getValue().get("path").replaceAll("/\\*\\*$", "/v2/api-docs"));
			swaggerResource.setSwaggerVersion("2.0");
			return swaggerResource;
		}).collect(Collectors.toList());
	}

	public Map<String, Map<String, String>> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<String, Map<String, String>> routes) {
		this.routes = routes;
	}

	public String getIgnoredPatterns() {
		return ignoredPatterns;
	}

	public void setIgnoredPatterns(String ignoredPatterns) {
		this.ignoredPatterns = ignoredPatterns;
	}

}