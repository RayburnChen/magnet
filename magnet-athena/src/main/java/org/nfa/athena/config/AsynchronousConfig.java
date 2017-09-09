package org.nfa.athena.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsynchronousConfig implements AsyncConfigurer {

	private static Logger log = LoggerFactory.getLogger(MyAsyncUncaughtExceptionHandler.class);

	@Override
	public Executor getAsyncExecutor() {
		ExecutorService executor = Executors.newFixedThreadPool(8);
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new MyAsyncUncaughtExceptionHandler();
	}

	public static class MyAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

		@Override
		public void handleUncaughtException(Throwable ex, Method method, Object... params) {
			log.error("AsyncUncaughtExceptionHandler method:{} params:{}", ex, method, params);
		}

	}
}
