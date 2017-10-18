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

	// grant type = password
	// org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter
	
	/*
	curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=password -d username=user -d password=88350ea4-cbf7-485f-a8ad-8d11ce3d7522 -d scope=write

	curl localhost:8110/greeting/oneUser -H "Authorization: Bearer 9dc9d55a-bbce-46d5-b09d-c505af67df20"

	curl magnet-client:passw0rd@localhost:8090/oauth/check_token -d token=9dc9d55a-bbce-46d5-b09d-c505af67df20
	*/
	
	public static void main(String[] args) {
		SpringApplication.run(MagnetAuthApplication.class, args);
	}

}
