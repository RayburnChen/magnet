package org.nfa.base.service.config;

import org.nfa.base.service.impl.CustomAccessTokenConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
public class ResourceServerTokenConfig {

	// org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration
	// ResourceServerTokenServicesConfiguration.RemoteTokenServicesConfiguration
	// ResourceServerTokenServicesConfiguration.JwtTokenServicesConfiguration

	@Configuration
	public static class RemoteTokenConfig implements InitializingBean {

		private final RemoteTokenServices remoteTokenServices;

		public RemoteTokenConfig(ObjectProvider<RemoteTokenServices> remoteTokenServicesProvider) {
			super();
			this.remoteTokenServices = remoteTokenServicesProvider.getIfAvailable();
		}

		@Override
		public void afterPropertiesSet() throws Exception {
			if (null != remoteTokenServices) {
				remoteTokenServices.setAccessTokenConverter(new CustomAccessTokenConverter());
			}
		}

	}

	@Configuration
	@ConditionalOnMissingBean(JwtAccessTokenConverter.class)
	public static class JwtTokenConfig {

		@Bean
		public JwtAccessTokenConverterConfigurer jwtAccessTokenConverterConfigurer() {
			return converter -> converter.setAccessTokenConverter(new CustomAccessTokenConverter());
		}

	}

}
