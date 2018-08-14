package org.nfa.lucia.config;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties("lucia")
@Validated
public class ConfigProperties implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(ConfigProperties.class);
	public static final String LUCENE_INDEX_PATH = "lucene_index_path";

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("ConfigProperties: {}", this);
	}

	@NotNull
	private Map<String, String> resources = new HashMap<>();

	public Map<String, String> getResources() {
		return resources;
	}

	public void setResources(Map<String, String> resources) {
		this.resources = resources;
	}

	@Override
	public String toString() {
		return "ConfigProperties [resources=" + resources + "]";
	}

}
