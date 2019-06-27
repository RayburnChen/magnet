package org.nfa.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
public class LoginConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.anyRequest()
				.authenticated()
			.and()
				.formLogin()//DefaultLoginPageGeneratingFilter
				.permitAll()
			.and()
				.logout()
				.permitAll()
			.and()
		    	.csrf()
		    	.disable();
	}
	
}