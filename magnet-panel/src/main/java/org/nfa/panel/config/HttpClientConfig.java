package org.nfa.panel.config;

import java.io.IOException;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.encoding.BaseRequestInterceptor;
import org.springframework.cloud.openfeign.encoding.FeignClientEncodingProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.client.RestTemplate;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
@EnableConfigurationProperties(FeignClientEncodingProperties.class)
public class HttpClientConfig {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, OAuth2ClientContext oAuth2ClientContext, OAuth2ProtectedResourceDetails resource) {
		return restTemplateBuilder.additionalInterceptors(new RestClientInterceptor()).configure(new OAuth2RestTemplate(resource, oAuth2ClientContext));
	}

	private class RestClientInterceptor implements ClientHttpRequestInterceptor {

		@Override
		public ClientHttpResponse intercept(HttpRequest req, byte[] body, ClientHttpRequestExecution execution) throws IOException {

			addHeaderIfNotExist(req, "Headers.G2_USER_ID", "id");
			addHeaderIfNotExist(req, "Headers.G2_USER_ROLES", "roles");

			return execution.execute(req, body);
		}

		private void addHeaderIfNotExist(HttpRequest req, String name, String value) {
			HttpHeaders headers = req.getHeaders();
			if (!headers.containsKey(name)) {
				headers.add(name, value);
			}
		}

	}

	@Bean
	public RequestInterceptor oAuth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext, OAuth2ProtectedResourceDetails resource) {
		return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, resource);
	}

	@Bean
	public RequestInterceptor customfeignInterceptor(FeignClientEncodingProperties properties) {

		// if use zipkin see
		// org.springframework.cloud.sleuth.instrument.web.client.feign.TraceLoadBalancerFeignClient
		// org.springframework.cloud.sleuth.instrument.web.client.feign.TracingFeignClient

		// 1. org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient

		// 2. org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer

		// 3. com.netflix.client.AbstractLoadBalancerAwareClient#executeWithLoadBalancer

		// 4. feign.Client.$Default

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
