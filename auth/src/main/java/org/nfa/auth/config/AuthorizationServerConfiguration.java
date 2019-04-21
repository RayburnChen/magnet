package org.nfa.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

//@Configuration
//@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
public class AuthorizationServerConfiguration extends AuthorizationServerSecurityConfiguration {
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthorizationServerEndpointsConfiguration endpoints;
	@Autowired
	private CorsConfigurationSource corsConfigurationSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.cors().configurationSource(corsConfigurationSource);
		if (!endpoints.getEndpointsConfigurer().isUserDetailsServiceOverride()) {
			endpoints.getEndpointsConfigurer().userDetailsService(this.userDetailsService);
		}
		super.configure(http);
	}

}
