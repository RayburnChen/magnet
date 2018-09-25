package org.nfa.zuul;

import org.nfa.panel.config.GlobalErrorController;
import org.nfa.panel.config.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableMongoAuditing
@EnableMongoRepositories
@Import(value = { GlobalErrorController.class, GlobalExceptionHandler.class })
@EnableOAuth2Sso
public class MagnetZuulApplication extends WebSecurityConfigurerAdapter {

	// http://localhost:8080/actuator/filters
	// http://localhost:8080/actuator/routes
	// http://localhost:8080/actuator/routes/details
	// http://localhost:8080/athena/greeting/oneUser

	public static void main(String[] args) {
		SpringApplication.run(MagnetZuulApplication.class, args);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/**")
				.authorizeRequests()
			.antMatchers("/permission/**")
				.permitAll()
			.anyRequest()
				.authenticated();
	}
	
	// org.springframework.cloud.security.oauth2.proxy.OAuth2TokenRelayFilter
	// org.springframework.cloud.netflix.zuul.web.ZuulController

}
