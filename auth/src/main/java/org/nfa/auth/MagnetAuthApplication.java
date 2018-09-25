package org.nfa.auth;

import org.nfa.base.service.config.GlobalErrorController;
import org.nfa.base.service.config.GlobalExceptionHandler;
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

	// Step 1
	// Resource Owner Password Credentials
	// User gives Password to Client
	// Get Token
	// Step 1.1
	// curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=password -d username=user -d password=qqq -d scope=read%20write
	// curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=password -d username=user -d password=qqq -d scope=read+write
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter
	
	// Step 2
	// Authorization Code Credentials
	// Client Server Redirect to Authorization Server and get Authorization Code
	// Step 2.1
	// Client Server Redirect and response_type is code
	// localhost:8090/oauth/authorize?client_id=magnet-client&response_type=code&redirect_uri=http://www.baidu.com
	
	// org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
	
	// Client Server Get Authorization Code
	// Step 2.2
	// https://www.baidu.com/?code=8EQXpz
	
	// Client Server use code to call Authorization Server and get token
	// Step 2.3
	// curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=authorization_code -d redirect_uri=http://www.baidu.com -d code=8EQXpz
	// Use token to call Resource server
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter
	
	// Step 3
	// Implicit Credentials
	// Client Server Redirect to Authorization Server
	// Step 3.1
	// response_type is token
	// localhost:8090/oauth/authorize?client_id=magnet-client&response_type=token&redirect_uri=http://www.baidu.com
	// Client Server get token by one call and no need to call Authorization Server again
	// Implicit will not validate Client Server because there is no Step 2.3 which need Client Server Secret
	// Implicit can get access token but can not get refresh token
	// Step 3.2
	// https://www.baidu.com/#access_token=5630cd7f-ed85-4d56-b5fc-bdd836a512ab&token_type=bearer&expires_in=43199&scope=read%20write
	
	// org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
	// org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter
	
	// Step 4
	// Client Credentials
	// Use Client id and secret to get token
	// Step 4.1
	// curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=client_credentials
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter
	
	// Step 5
	// Refresh Token Credentials
	// When access token is about to expire, use the refresh token to get the new access token
	// Step 5.1
	// curl magnet-client:passw0rd@localhost:8090/oauth/token -d grant_type=refresh_token -d refresh_token=632ba621-540f-41bb-8989-46a22e2702c3
	
	// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
	// org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter
	
	public static void main(String[] args) {
		SpringApplication.run(MagnetAuthApplication.class, args);
	}
	
	// Case new user
	
	// 1. Call Zuul GET http://localhost:8080/athena/greeting/oneUser
	//    Zuul check cookie failed and return Set-Cookie: magnet-zuul=9ScfS1QftDI9u4zOmtHLLOQSVSIhM5DsPxD8k5eB
	
	// 2. Redirect Zuul GET http://localhost:8080/login
	//    Zuul return header Location: http://localhost:8090/oauth/authorize?client_id=magnet-client&redirect_uri=http://localhost:8080/login&response_type=code&state=6ePbq7
	
	// 3. Redirect Auth GET http://localhost:8090/oauth/authorize?client_id=magnet-client&redirect_uri=http://localhost:8080/login&response_type=code&state=6ePbq7
	//    Auth check cookie failed and return Set-Cookie: magnet-auth=jMnbmufM-F3fwztcdSUefJwZzSklDScBwduooofp
	
	// 4. Redirect Auth GET http://localhost:8090/login
	//    Auth return login page html
	
	// 5. Post password POST http://localhost:8090/login
	//    Auth return Set-Cookie: magnet-auth=0MLmzi8CxqtSYBw_pWkPJkc9sGgY6a_w2EMUcKNv
	
	// 6. Redirect Auth GET http://localhost:8090/oauth/authorize?client_id=magnet-client&redirect_uri=http://localhost:8080/login&response_type=code&state=6ePbq7
	//    Auth return Location: http://localhost:8080/login?code=1iXiQZ&state=6ePbq7
	
	// 7. Redirect Zuul GET http://localhost:8080/login?code=1iXiQZ&state=6ePbq7
	//    Zuul return Set-Cookie: magnet-zuul=mDdJaBoWwGR6w6xxrK6IOUpKN-KiJijISYL9E-LB
	
	// 8. Redirect Zuul GET http://localhost:8080/athena/greeting/oneUser
	//    Forward to Athena service and return
	
	// Case old user
	// Call Zuul GET http://localhost:8080/athena/greeting/oneUser
	// Cookie: magnet-zuul=mDdJaBoWwGR6w6xxrK6IOUpKN-KiJijISYL9E-LB; magnet-auth=0MLmzi8CxqtSYBw_pWkPJkc9sGgY6a_w2EMUcKNv
	// Forward to Athena service and return
	
}
