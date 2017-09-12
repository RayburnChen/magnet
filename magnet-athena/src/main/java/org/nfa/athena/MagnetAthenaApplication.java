package org.nfa.athena;

import org.nfa.athena.config.WebSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
//@EnableBinding(MagnetSource.class)
@EnableResourceServer
@Import({WebSecurityConfiguration.class})
public class MagnetAthenaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnetAthenaApplication.class, args);
	}
	
}
