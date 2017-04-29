package org.nfa.lucia.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.netflix.hystrix.exception.HystrixRuntimeException;

import feign.FeignException;

@ControllerAdvice
@ResponseBody
public class ExceptionReaper {

	@ExceptionHandler(value = { HystrixRuntimeException.class })
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public Map<String, Object> hystrixRuntimeException(HystrixRuntimeException exception, WebRequest request) {
		if (exception.getCause() instanceof FeignException) {
			exception.getCause().printStackTrace();
		}
		Map<String, Object> response = new HashMap<>();
		response.put("message", exception.getCause().getMessage());
		return response;
	}
	
}
