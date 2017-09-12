package org.nfa.athena.service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class AsyncService<T, R> {

	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(8);

	private List<Future<T>> results = new LinkedList<>();
	private final Function<Stream<T>, R> callback;

	private AsyncService(Function<Stream<T>, R> callback) {
		super();
		this.callback = callback;
	}

	public static <T, R> AsyncService<T, R> callback(Function<Stream<T>, R> callback) {
		return new AsyncService<T, R>(callback);
	}

	public AsyncService<T, R> after(Supplier<T> c) {
		return afterDoing(c);
	}

	public <I> AsyncService<T, R> after(Function<I, T> f, I in) {
		return afterDoing(() -> f.apply(in));
	}

	public <I> AsyncService<T, R> after(Consumer<I> c, I in) {
		return afterDoing(() -> {
			c.accept(in);
			return null;
		});
	}

	public <I> AsyncService<T, R> after(Consumer<I> c) {
		return afterDoing(() -> {
			c.accept(null);
			return null;
		});
	}

	private AsyncService<T, R> afterDoing(Supplier<T> c) {
		results.add(EXECUTOR.submit(new Callable<T>() {
			@Override
			public T call() throws Exception {
				return c.get();
			}
		}));
		return this;
	}

	public R getResult() {
		return callback.apply(results.stream().map(r -> get(r)));
	}

	private T get(Future<T> r) {
		try {
			return r.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
