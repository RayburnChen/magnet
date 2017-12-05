package org.nfa.athena.service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.nfa.athena.EnableMagnetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EnableMagnetService(value = "AsyncService", priority = 1, comments = "Test Comments")
public class AsyncService<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncService.class);
	private static final long TIMEOUT_SECONDS = 10L;
	private static final ExecutorService EXECUTOR = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private List<Future<T>> results = new LinkedList<>();

	private AsyncService() {
		super();
	}

	public static <T> AsyncService<T> build() {
		return new AsyncService<T>();
	}

	public Future<T> add(Runnable runnable) {
		return addDoing(() -> {
			runnable.run();
			return null;
		});
	}

	public Future<T> add(Supplier<T> supplier) {
		return addDoing(supplier);
	}

	private Future<T> addDoing(Supplier<T> c) {
		Future<T> future = EXECUTOR.submit(new Callable<T>() {
			@Override
			public T call() throws Exception {
				// prevent memory leak
				// add AUTH
				T t = c.get();
				// clear AUTH
				return t;
			}
		});
		results.add(future);
		return future;
	}

	public List<T> getResult() {
		return results.stream().map(r -> get(r)).filter(r -> null != r).collect(Collectors.toList());
	}

	private T get(Future<T> r) {
		try {
			return r.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			LOGGER.error("Dependency Service is not available" + e.toString(), e);
			throw new RuntimeException(e);
		}
	}

}
