package org.nfa.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
public class AuthorizationServer implements AuthorizationServerConfigurer {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private TokenStore tokenStore;
//	@Autowired
//	private AccessTokenConverter accessTokenConverter;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security
			.passwordEncoder(passwordEncoder)
			.checkTokenAccess("fullyAuthenticated")
			.tokenKeyAccess("fullyAuthenticated");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
			.inMemory()
			.withClient("magnet-client")
			.authorizedGrantTypes("password", "client_credentials", "refresh_token")
			.secret(passwordEncoder.encode("passw0rd"))
			.scopes("read", "write");
//			.accessTokenValiditySeconds(-1)
//			.refreshTokenValiditySeconds(-1);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore)
//			.accessTokenConverter(accessTokenConverter)
			.authenticationManager(authenticationConfiguration.getAuthenticationManager())
			.userDetailsService(userDetailsService);
	}

}
