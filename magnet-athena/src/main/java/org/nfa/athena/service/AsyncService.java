package org.nfa.athena.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.nfa.athena.EnableMagnetService;
import org.nfa.common.ApplicationException;
import org.nfa.common.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EnableMagnetService(value = "AsyncService", priority = 1, comments = "Test Comments")
public class AsyncService<T> {

	public static final long DEFAULT_TIMEOUT = 30L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncService.class);
	private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
	private static final int POOL_SIZE = PROCESSORS * 2 > 8 ? PROCESSORS * 2 : 8;
	private static final int QUEUE_SIZE = 10000;
	private static final Comparator<Runnable> COMP = initComp();
	private static final ExecutorService EXECUTOR = new CustomThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(QUEUE_SIZE, COMP));

	private final long timeoutSeconds;
	private List<Future<T>> results = new ArrayList<>();

	private AsyncService(long timeoutSeconds) {
		super();
		this.timeoutSeconds = timeoutSeconds;
	}

	public static <T> AsyncService<T> build() {
		return build(DEFAULT_TIMEOUT);
	}

	public static <T> AsyncService<T> build(long timeoutSeconds) {
		return new AsyncService<T>(timeoutSeconds);
	}

	public CustomFuture<T> add(Runnable runnable) {
		return addDoing(Priority.MINOR, toCallable(runnable));
	}

	public CustomFuture<T> add(int priority, Runnable runnable) {
		return addDoing(priority, toCallable(runnable));
	}

	public CustomFuture<T> add(Callable<T> callable) {
		return addDoing(Priority.MINOR, callable);
	}

	public CustomFuture<T> add(int priority, Callable<T> callable) {
		return addDoing(priority, callable);
	}

	private CustomFuture<T> addDoing(int priority, Callable<T> callable) {
		CustomCallable<T> task = new CustomCallable<T>(priority, callable);
		Future<T> future = EXECUTOR.submit(task);
		this.results.add(future);
		return new CustomFuture<>(future, this.timeoutSeconds);
	}

	public List<T> getResult() {
		return getResult(this.timeoutSeconds);
	}

	public List<T> getResult(long timeoutSeconds) {
		return results.stream().map(r -> getResult(r, timeoutSeconds)).filter(r -> null != r).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	private T getResult(Future<T> future, long timeOutSeconds) {
		return (T) get(future, timeOutSeconds);
	}

	private Callable<T> toCallable(Runnable runnable) {
		return new Callable<T>() {
			@Override
			public T call() throws Exception {
				runnable.run();
				return null;
			}
		};
	}

	private static Object get(Future<?> future, long timeOutSeconds) {
		try {
			if (timeOutSeconds < 0L) {
				return future.get();
			} else {
				return future.get(timeOutSeconds, TimeUnit.SECONDS);
			}
		} catch (TimeoutException e) {
			LOGGER.error("Dependency Service is time out " + e.toString(), e);
			throw new ApplicationException("Dependency Service time out after " + timeOutSeconds + " seconds", 10023012, e);
		} catch (InterruptedException | ExecutionException | RuntimeException e) {
			LOGGER.error("Dependency Service is not available " + e.toString(), e);
			Throwable th = e.getCause();
			if (th instanceof ApplicationException) {
				throw (ApplicationException) th;
			} else if (null != th) {
				throw new ApplicationException("Dependency Service not available " + th.getMessage(), 10023012, th);
			} else {
				throw new ApplicationException("Dependency Service not available " + e.toString(), 10023012, e);
			}
		}
	}

	public static class CustomFuture<T> {

		public CustomFuture(Future<T> future, long timeoutSeconds) {
			super();
			this.future = future;
			this.timeoutSeconds = timeoutSeconds;
		}

		private final Future<T> future;
		private final long timeoutSeconds;

		public T getResult() {
			return getResult(this.timeoutSeconds);
		}

		public T getResult(long timeoutSeconds) {
			return getResult(future, timeoutSeconds);
		}

		@SuppressWarnings("unchecked")
		private T getResult(Future<T> future, long timeOutSeconds) {
			return (T) get(future, timeOutSeconds);
		}

	}

	public static class CustomCallable<T> implements Callable<T> {

		private CustomCallable(int priority, Callable<T> task) {
			super();
			this.priority = priority;
			this.task = task;
		}

		private final int priority;
		private final Callable<T> task;
		// private final String id = AuthUtils.getId();
		// private final String token = AuthUtils.getToken();
		// private final String name = AuthUtils.getName();
		// private final String station = AuthUtils.getStation();
		// private final String console = AuthUtils.getConsole();
		// private final String supervisor = AuthUtils.getSupervisor();
		// private final String roles = AuthUtils.getRoles();

		private int getPriority() {
			return priority;
		}

		@Override
		public T call() throws Exception {
			// try {
			// AuthUtils.setUser();
			return task.call();
			// } finally {
			// AuthUtils.remove();
			// }
		}

	}

	private static Comparator<Runnable> initComp() {
		return (r1, r2) -> getPriority(r1) - getPriority(r2);
	}

	// task is CustomFutureTask
	private static int getPriority(Runnable task) {
		CustomFutureTask<?> CustomFutureTask = (CustomFutureTask<?>) task;
		int priority = CustomFutureTask.getPriority();
		return priority;
	}

	public static class CustomThreadPoolExecutor extends ThreadPoolExecutor {

		public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		// CustomCallable is not a Runnable, suppose useless
		protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
			return new CustomFutureTask<T>(runnable, value, Priority.LOWEST);
		}

		protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
			CustomCallable<T> CustomCallable = (CustomCallable<T>) callable;
			return new CustomFutureTask<T>(callable, CustomCallable.getPriority());
		}

	}

	public static class CustomFutureTask<T> extends FutureTask<T> {

		private final int priority;

		private int getPriority() {
			return priority;
		}

		public CustomFutureTask(Callable<T> callable, int priority) {
			super(callable);
			this.priority = priority;
		}

		public CustomFutureTask(Runnable runnable, T result, int priority) {
			super(runnable, result);
			this.priority = priority;
		}

		@Override
		protected void done() {
			try {
				if (!isCancelled()) {
					super.get();
				}
			} catch (RuntimeException | InterruptedException | ExecutionException e) {
				LOGGER.error("Dependency Service is not available " + e.toString(), e);
			}
		}

	}

}
