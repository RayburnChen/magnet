package org.nfa.auth;

import org.nfa.panel.config.GlobalErrorController;
import org.nfa.panel.config.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableMongoAuditing
@EnableMongoRepositories
@EnableDiscoveryClient
@EnableAuthorizationServer
@Import(value = { GlobalErrorController.class, GlobalExceptionHandler.class })
public class MagnetAuthApplication {
	
	// Spring Boot Authorization Server Configuration
	// org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration
	
	// Resource Server
	// Call Authorization Server to Check Token
	// curl magnet-client:passw0rd@localhost:8090/oauth/check_token -d token=67e195e9-89bf-41c3-a107-fc0b24a6674a
	// org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint
	
	// Resource Server Sample Request
	// curl localhost:8110/greeting/oneUser -H "Authorization: Bearer 67e195e9-89bf-41c3-a107-fc0b24a6674a"

	// Mode 1
	// Resource Owner Password Credentials
	// User gives Password to Client
	// Get Token
	// Mode 1.1
	// curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=password -d username=user -d password=qqq -d scope=read%20write
	// curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=password -d username=user -d password=qqq -d scope=read+write
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter
	
	// Mode 2
	// Authorization Code Credentials
	// Client Server Redirect to Authorization Server and get Authorization Code
	// Mode 2.1
	// Client Server Redirect and response_type is code
	// localhost:8090/oauth/authorize?client_id=magnet-client&response_type=code&redirect_uri=http://www.baidu.com
	
	// org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
	
	// Client Server Get Authorization Code
	// Mode 2.2
	// https://www.baidu.com/?code=8EQXpz
	
	// Client Server use code to call Authorization Server and get token
	// Mode 2.3
	// curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=authorization_code -d redirect_uri=http://www.baidu.com -d code=8EQXpz
	// Use token to call Resource server
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter
	
	// Mode 3
	// Implicit Credentials
	// Client Server Redirect to Authorization Server
	// Mode 3.1
	// response_type is token
	// localhost:8090/oauth/authorize?client_id=magnet-client&response_type=token&redirect_uri=http://www.baidu.com
	// Client Server get token by one call and no need to call Authorization Server again
	// Implicit will not validate Client Server because there is no Mode 2.3 which need Client Server Secret
	// Implicit can get access token but can not get refresh token
	// Mode 3.2
	// https://www.baidu.com/#access_token=5630cd7f-ed85-4d56-b5fc-bdd836a512ab&token_type=bearer&expires_in=43199&scope=read%20write
	
	// org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
	// org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter
	
	// Mode 4
	// Client Credentials
	// Use Client id and secret to get token
	// Mode 4.1
	// curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=client_credentials
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter
	
	// Mode 5
	// Refresh Token Credentials
	// When access token is about to expire, use the refresh token to get the new access token
	// Mode 5.1
	// curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=refresh_token -d refresh_token=632ba621-540f-41bb-8989-46a22e2702c3
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter
	
	public static void main(String[] args) {
		SpringApplication.run(MagnetAuthApplication.class, args);
	}

}
