package org.nfa.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAuthorizationServer
@EnableSwagger2
public class MagnetAuthApplication {

	// org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint

	public static void main(String[] args) {
		SpringApplication.run(MagnetAuthApplication.class, args);
	}

}
