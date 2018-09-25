package org.nfa.base.service.config;

import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorConfig {

	private static final Logger log = LoggerFactory.getLogger(ActuatorConfig.class);

	@Bean
	public InfoContributor netInfo() {
		return new InfoContributor() {

			@Override
			public void contribute(Builder builder) {
				try {
					builder.withDetail("address", InetAddress.getLocalHost().toString());
				} catch (IOException e) {
					log.error("Get InetAddress failed", e);
				}
			}
		};
	}

}
