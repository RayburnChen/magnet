package org.nfa.lucia.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.feign.encoding.BaseRequestInterceptor;
import org.springframework.cloud.netflix.feign.encoding.FeignClientEncodingProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
@EnableConfigurationProperties(FeignClientEncodingProperties.class)
public class FeignClientConfig {

	@Bean
	public RequestInterceptor oauthFeignInterceptor(OAuth2ClientContext oAuth2ClientContext,
			OAuth2ProtectedResourceDetails resource) {
		return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, resource);
	}

	@Bean
	public RequestInterceptor customfeignInterceptor(FeignClientEncodingProperties properties) {
		return new FeignInterceptor(properties);
	}

	private class FeignInterceptor extends BaseRequestInterceptor {

		protected FeignInterceptor(FeignClientEncodingProperties properties) {
			super(properties);
		}

		@Override
		public void apply(RequestTemplate template) {
			addHeader(template, "MY_HEADER", "MY_HEADER_VALUE");
		}

	}

}
