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
	curl magnet-client:passw0rd@localhost:8090/magnet-auth/oauth/token -d grant_type=password -d username=user -d password=qqq -d scope=write

	// call resource server
	curl localhost:8110/magnet-athena/greeting/oneUser -H "Authorization: Bearer 27e95bbb-e26c-4c45-aa76-1ea3f3fb2dc6"
	// resource server check token
	curl magnet-client:passw0rd@localhost:8090/magnet-auth/oauth/check_token -d token=27e95bbb-e26c-4c45-aa76-1ea3f3fb2dc6

	// client server use user credential to authorize resource
	 * 
	curl user:qqq@localhost:8090/magnet-auth/oauth/authorize -d client_id=magnet-client -d response_type=code -d redirect_uri=www.baidu.com
	localhost:8090/magnet-auth/oauth/authorize?client_id=magnet-client&response_type=code&redirect_uri=http://www.baidu.com
	
	// client server get token
	curl magnet-client:passw0rd@localhost:8090/magnet-auth/oauth/token -d grant_type=authorization_code -d client_id=magnet-client -d redirect_uri=http://www.baidu.com -d code=et4hqo

	org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter
	org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
	 */
	
	// RemoteTokenServices.loadAuthentication
	
	public static void main(String[] args) {
		SpringApplication.run(MagnetAuthApplication.class, args);
	}

}
