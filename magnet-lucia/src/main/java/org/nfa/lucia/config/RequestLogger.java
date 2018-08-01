package org.nfa.lucia.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class RequestLogger implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(RequestLogger.class);
	private static final String REQUEST_LOGGER_TIME = "REQUEST_LOGGER_TIME";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info("Received {}:{}", request.getMethod(), request.getRequestURI());
		request.setAttribute(REQUEST_LOGGER_TIME, System.currentTimeMillis());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		long time = Long.parseLong(String.valueOf(request.getAttribute(REQUEST_LOGGER_TIME)));
		log.info("Complete {}:{} after {}ms", request.getMethod(), request.getRequestURI(), System.currentTimeMillis() - time);
		request.removeAttribute(REQUEST_LOGGER_TIME);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}