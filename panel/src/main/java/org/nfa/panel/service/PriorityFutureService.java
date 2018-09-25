package org.nfa.panel.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.nfa.base.ApplicationException;
import org.nfa.base.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriorityFutureService<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PriorityFutureService.class);
	private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
	private static final int POOL_SIZE = PROCESSORS * 2 > 8 ? PROCESSORS * 2 : 8;
	private static final int QUEUE_SIZE = 10000;
	private static final Comparator<Runnable> COMP = initComp();
	private static final ExecutorService EXECUTOR = new PriorityThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
			new PriorityBlockingQueue<Runnable>(QUEUE_SIZE, COMP));

	private List<CompletableFuture<T>> objectFutureList = new ArrayList<>();
	private List<CompletableFuture<Void>> voidFutureList = new ArrayList<>();

	private PriorityFutureService() {
		super();
	}

	public static <T> PriorityFutureService<T> build() {
		return new PriorityFutureService<T>();
	}

	public CompletableFuture<Void> add(Runnable runnable) {
		return add(runnable, Priority.MINOR);
	}

	public CompletableFuture<T> add(Supplier<T> supplier) {
		return add(supplier, Priority.MINOR);
	}

	public CompletableFuture<Void> add(Runnable runnable, int priority) {
		PriorityRunnable priorityRunnable = new PriorityRunnable(runnable, priority);
		CompletableFuture<Void> future = CompletableFuture.runAsync(priorityRunnable, EXECUTOR);
		voidFutureList.add(future);
		return future;
	}

	public CompletableFuture<T> add(Supplier<T> supplier, int priority) {
		PrioritySupplier<T> prioritySupplier = new PrioritySupplier<>(supplier, priority);
		CompletableFuture<T> future = CompletableFuture.supplyAsync(prioritySupplier, EXECUTOR);
		objectFutureList.add(future);
		return future;
	}

	public List<T> getResult() {
		return this.objectFutureList.stream().map(this::get).collect(Collectors.toList());
	}

	public void getVoidResult() {
		voidFutureList.forEach(this::get);
	}

	private <V> V get(CompletableFuture<V> future) {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException | RuntimeException e) {
			LOGGER.error("Dependency Service unavailable " + e.toString(), e);
			Throwable th = e.getCause();
			if (th instanceof ApplicationException) {
				throw (ApplicationException) th;
			} else if (null != th) {
				throw new ApplicationException("Dependency Service unavailable " + th.getMessage(), 10023012, th);
			} else {
				throw new ApplicationException("Dependency Service unavailable " + e.toString(), 10023012, e);
			}
		}
	}

	private static class PriorityRunnable implements Runnable {

		private final Runnable runnable;
		private final int priority;

		public PriorityRunnable(Runnable runnable, int priority) {
			super();
			this.runnable = runnable;
			this.priority = priority;
		}

		public int getPriority() {
			return priority;
		}

		@Override
		public void run() {
			runnable.run();
		}

	}

	private static class PrioritySupplier<T> implements Supplier<T> {

		private final Supplier<T> supplier;
		private final int priority;

		public PrioritySupplier(Supplier<T> supplier, int priority) {
			super();
			this.supplier = supplier;
			this.priority = priority;
		}

		public int getPriority() {
			return priority;
		}

		@Override
		public T get() {
			return supplier.get();
		}

	}

	private static class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

		public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		@Override
		public void execute(Runnable command) {
			PriorityRunnable priorityRunnable = new PriorityRunnable(command, getPriority(command));
			super.execute(priorityRunnable);
		}

		private int getPriority(Runnable command) {
			try {
				// command is CompletableFuture$AsyncSupply
				Field fn = command.getClass().getDeclaredField("fn");
				fn.setAccessible(true);
				Object o = fn.get(command);
				if (o instanceof PrioritySupplier) {
					PrioritySupplier<?> prioritySupplier = (PrioritySupplier<?>) o;
					return prioritySupplier.getPriority();
				} else if (o instanceof PriorityRunnable) {
					PriorityRunnable priorityRunnable = (PriorityRunnable) o;
					return priorityRunnable.getPriority();
				} else {
					return Priority.MINOR;
				}
			} catch (Exception e) {
				return Priority.MINOR;
			}
		}

	}

	private static Comparator<Runnable> initComp() {
		return (r1, r2) -> getPriority(r1) - getPriority(r2);
	}

	// task is PriorityRunnable
	private static int getPriority(Runnable task) {
		PriorityRunnable priorityFutureTask = (PriorityRunnable) task;
		int priority = priorityFutureTask.getPriority();
		return priority;
	}

}
