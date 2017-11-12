package org.nfa.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
public class MagnetAuthApplication {
	
	// Spring Boot Authorization Server Configuration
	// org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration
	
	// Resource Server
	// Call Authorization Server to Check Token
	// curl magnet-client:passw0rd@localhost:8090/magnet-auth/oauth/check_token -d token=30bca85a-3ee0-4eab-bb8f-39eb945f1783
	// org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint
	
	// Resource Server Sample Request
	// curl localhost:8110/magnet-athena/greeting/oneUser -H "Authorization: Bearer 30bca85a-3ee0-4eab-bb8f-39eb945f1783"

	// Mode 1
	// Resource Owner Password Credentials
	// User gives Password to Client
	// Get Token
	// Mode 1.1
	// curl magnet-client:passw0rd@localhost:8090/magnet-auth/oauth/token -d grant_type=password -d username=user -d password=qqq -d scope=read%write
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter
	
	// Mode 2
	// Authorization Code Credentials
	// Client Server Redirect to Authorization Server and get Authorization Code
	// Mode 2.1
	// curl user:qqq@localhost:8090/magnet-auth/oauth/authorize -d client_id=magnet-client -d response_type=code -d redirect_uri=www.baidu.com
	// localhost:8090/magnet-auth/oauth/authorize?client_id=magnet-client&response_type=code&redirect_uri=http://www.baidu.com
	
	// org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
	
	// Client Server Get Authorization Code
	// Mode 2.2
	// https://www.baidu.com/?code=SsQH1R
	
	// Client Server use code to call Authorization Server and get token
	// Mode 2.3
	// curl magnet-client:passw0rd@localhost:8090/magnet-auth/oauth/token -d grant_type=authorization_code -d redirect_uri=http://www.baidu.com -d code=SsQH1R
	// Use token to call Resource server
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter
	
	// Mode 3
	// Implicit Credentials
	// Client Server Redirect to Authorization Server
	// Mode 3.1
	// localhost:8090/magnet-auth/oauth/authorize?client_id=magnet-client&redirect_uri=http://localhost:8100/magnet-lucia/login&response_type=code&state=RRRwEg
	// Client Server get token by one call and no need to call Authorization Server again
	// Implicit will not validate Client Server because there is no Mode 2.3 which need Client Server Secret
	// Implicit can get access token but can not get refresh token
	
	// org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
	// org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter
	
	// Mode 4
	// Client Credentials
	// Use Client id and secret to get token
	// Mode 4.1
	// curl magnet-client:passw0rd@localhost:8090/magnet-auth/oauth/token -d grant_type=client_credentials
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter
	
	// Mode 5
	// Refresh Token Credentials
	// When access token is about to expire, use the refresh token to get the new access token
	// Mode 5.1
	// curl magnet-client:passw0rd@localhost:8090/magnet-auth/oauth/token -d grant_type=refresh_token -d refresh_token=d845a999-5ce0-440a-a6ea-9d845523db9b
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter
	
	public static void main(String[] args) {
		SpringApplication.run(MagnetAuthApplication.class, args);
	}

}
