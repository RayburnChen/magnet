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
	 
	// get token
	curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=password -d username=user -d password=3c2c3bee-35ae-4af4-bc97-cf7ee4415a7d -d scope=write

	// call resource server
	curl localhost:8110/greeting/oneUser -H "Authorization: Bearer 27e95bbb-e26c-4c45-aa76-1ea3f3fb2dc6"
	// resource server check token
	curl magnet-client:passw0rd@localhost:8090/oauth/check_token -d token=27e95bbb-e26c-4c45-aa76-1ea3f3fb2dc6

	// client server use user credential to authorize resource
	curl user:3c2c3bee-35ae-4af4-bc97-cf7ee4415a7d@localhost:8090/oauth/authorize -d client_id=magnet-client -d response_type=code -d redirect_uri=www.baidu.com

	 */
	
	// RemoteTokenServices.loadAuthentication
	
	public static void main(String[] args) {
		SpringApplication.run(MagnetAuthApplication.class, args);
	}

}
