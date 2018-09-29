package org.nfa.base.service.config;

import org.nfa.base.service.impl.CustomAccessTokenConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@ConditionalOnMissingBean(JwtAccessTokenConverter.class)
public class ResourceServerJwtTokenConfig {

	// org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration
	// ResourceServerTokenServicesConfiguration.RemoteTokenServicesConfiguration
	// ResourceServerTokenServicesConfiguration.JwtTokenServicesConfiguration

	@Bean
	public JwtAccessTokenConverterConfigurer jwtAccessTokenConverterConfigurer() {
		return converter -> converter.setAccessTokenConverter(new CustomAccessTokenConverter());
	}
	
}
