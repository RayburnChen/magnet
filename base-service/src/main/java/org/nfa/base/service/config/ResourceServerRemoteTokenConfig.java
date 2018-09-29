package org.nfa.base.service.config;

import org.nfa.base.service.impl.CustomAccessTokenConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
public class ResourceServerRemoteTokenConfig implements InitializingBean {

	private final RemoteTokenServices remoteTokenServices;

	public ResourceServerRemoteTokenConfig(ObjectProvider<RemoteTokenServices> remoteTokenServicesProvider) {
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