package org.nfa.panel.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(MappingJackson2HttpMessageConverter jackson2HttpMessageConverter) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new RestClientInterceptor());
		restTemplate.getMessageConverters().add(jackson2HttpMessageConverter);
		return restTemplate;
	}

	public static class RestClientInterceptor implements ClientHttpRequestInterceptor {

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

}
