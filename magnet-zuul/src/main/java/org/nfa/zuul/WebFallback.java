package org.nfa.zuul;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.nfa.base.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebFallback implements FallbackProvider {

	private static final Logger log = LoggerFactory.getLogger(WebFallback.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public String getRoute() {
		return "*";
	}

	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		log.error("Error while processing " + route, cause);
		return new ClientHttpResponse() {
			private HttpStatus status = HttpStatus.BAD_GATEWAY;

			@Override
			public HttpStatus getStatusCode() throws IOException {
				return status;
			}

			@Override
			public int getRawStatusCode() throws IOException {
				return status.value();
			}

			@Override
			public String getStatusText() throws IOException {
				return status.name();
			}

			@Override
			public void close() {

			}

			@Override
			public InputStream getBody() throws IOException {
				ErrorResponse error = new ErrorResponse();
				error.setCode(10000);
				error.setPath(route);
				error.setMessage(cause.getMessage());
				error.setException(cause.getClass().getSimpleName());
				ResponseEntity<ErrorResponse> response = new ResponseEntity<ErrorResponse>(error, HttpStatus.SERVICE_UNAVAILABLE);
				return new ByteArrayInputStream(objectMapper.writeValueAsBytes(response));
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				return headers;
			}
		};
	}

}