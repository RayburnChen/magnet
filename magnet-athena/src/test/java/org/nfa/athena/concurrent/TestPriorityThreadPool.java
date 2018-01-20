package org.nfa.athena.concurrent;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestPriorityThreadPool {

	private static final int SIZE = 100;
	private static final Comparator<Runnable> COMP = initComp();
	private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
			new PriorityBlockingQueue<Runnable>(SIZE, COMP));
	private static final Random RAN = new Random();

	@Test
	public void test() throws InterruptedException {
		for (int i = 0; i < SIZE; i++) {
			EXECUTOR.submit(new Task(RAN.nextInt(SIZE * 2)));
		}
		EXECUTOR.shutdown();
		EXECUTOR.awaitTermination(1, TimeUnit.HOURS);
	}

	public static class Task implements Callable<String> {

		private Task(int priority) {
			super();
			this.priority = priority;
		}

		private final int priority;

		public int getPriority() {
			return priority;
		}

		@Override
		public String call() throws Exception {
			TimeUnit.MILLISECONDS.sleep(RAN.nextInt(100));
			System.out.println(Thread.currentThread().getName() + " priority is " + this.priority);
			return "Done";
		}

	}

	private static Comparator<Runnable> initComp() {
		return (r1, r2) -> getPriority(r1) - getPriority(r2);
	}

	// task is java.util.concurrent.FutureTask
	private static int getPriority(Runnable task) {
		try {
			Field c = task.getClass().getDeclaredField("callable");
			c.setAccessible(true);
			Object callable = c.get(task);
			Field p = callable.getClass().getDeclaredField("priority");
			p.setAccessible(true);
			return p.getInt(callable);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return Integer.MAX_VALUE;
		}
	}

}
