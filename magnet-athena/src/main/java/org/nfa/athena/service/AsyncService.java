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

public class AsyncService<R> {

	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(8);

	private List<Callable<?>> tasks = new LinkedList<>();
	private List<Future<?>> results = new LinkedList<>();
	private final Supplier<R> callback;

	private AsyncService(Supplier<R> callback) {
		super();
		this.callback = callback;
	}

	public static <R> AsyncService<R> callback(Supplier<R> callback) {
		return new AsyncService<R>(callback);
	}

	public <V> AsyncService<R> after(Supplier<V> c) {
		tasks.add(new Callable<V>() {
			@Override
			public V call() throws Exception {
				return c.get();
			}
		});
		return this;
	}

	public <V, T> AsyncService<R> after(Function<T, V> f, T in) {
		tasks.add(new Callable<V>() {
			@Override
			public V call() throws Exception {
				return f.apply(in);
			}
		});
		return this;
	}

	public <V, T> AsyncService<R> after(Consumer<T> c, T in) {
		tasks.add(new Callable<V>() {
			@Override
			public V call() throws Exception {
				c.accept(in);
				return null;
			}
		});
		return this;
	}

	public <V, T> AsyncService<R> after(Consumer<T> c) {
		tasks.add(new Callable<V>() {
			@Override
			public V call() throws Exception {
				c.accept(null);
				return null;
			}
		});
		return this;
	}

	public R execute() {
		tasks.forEach(t -> results.add(EXECUTOR.submit(t)));
		results.forEach(r -> {
			try {
				r.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});
		return callback.get();
	}

}
