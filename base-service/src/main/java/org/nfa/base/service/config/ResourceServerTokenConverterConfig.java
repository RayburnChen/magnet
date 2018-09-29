package org.nfa.base.service.config;

import org.nfa.base.service.impl.CustomAccessTokenConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
public class ResourceServerTokenConverterConfig implements InitializingBean {

	private final RemoteTokenServices remoteTokenServices;

	public ResourceServerTokenConverterConfig(RemoteTokenServices remoteTokenServices) {
		super();
		this.remoteTokenServices = remoteTokenServices;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		remoteTokenServices.setAccessTokenConverter(new CustomAccessTokenConverter());
	}

}
