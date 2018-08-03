package org.nfa.lucia.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class ClientRequestInterceptor implements ClientHttpRequestInterceptor {
	
	// restTemplate.getInterceptors().add(new ClientRequestInterceptor());

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
