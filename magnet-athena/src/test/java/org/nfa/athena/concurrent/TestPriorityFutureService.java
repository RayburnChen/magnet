package org.nfa.athena.concurrent;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.nfa.panel.service.PriorityFutureService;

public class TestPriorityFutureService {

	private Random random = new Random();

	// need to change PriorityFutureService POOL_SIZE to 1

	@Test
	public void testCallable() {
		PriorityFutureService<String> priorityFutureService = PriorityFutureService.build();
		for (int i = 0; i < 10; i++) {
			int p = random.nextInt(999);
			priorityFutureService.add(() -> {
				try {
					String th = Thread.currentThread().toString();
					System.out.println(th + "start priority:" + p);
					TimeUnit.SECONDS.sleep(random.nextInt(3));
					System.out.println(th + " done priority:" + p);
					return th + " done";
				} catch (InterruptedException e) {
					e.printStackTrace();
					return "Failed";
				}
			}, p);
		}
		System.err.println(priorityFutureService.getResult());
	}

	@Test
	public void testRunnable() {
		PriorityFutureService<String> priorityFutureService = PriorityFutureService.build();
		for (int i = 0; i < 10; i++) {
			int p = random.nextInt(999);
			priorityFutureService.add(new Runnable() {

				@Override
				public void run() {
					try {
						String th = Thread.currentThread().toString();
						System.out.println(th + "start priority:" + p);
						TimeUnit.SECONDS.sleep(random.nextInt(3));
						System.out.println(th + " done priority:" + p);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}, p);
		}
		priorityFutureService.getVoidResult();
	}

	@Test
	public void testForkJoinPool() throws InterruptedException {
		ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
		System.out.println("size is " + forkJoinPool.getPoolSize());
		for (int i = 0; i < 20; i++) {
			forkJoinPool.submit(() -> {
				String th = Thread.currentThread().toString();
				System.out.println(th + " start");
				try {
					ForkJoinTask<String> rest = forkJoinPool.submit(() -> {
						TimeUnit.SECONDS.sleep(3);
						return Thread.currentThread().toString() + " task finish";
					});
					System.err.println(rest.get());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				System.out.println(th + " done");
			});
		}
		TimeUnit.SECONDS.sleep(3);
		System.out.println("size is " + forkJoinPool.getPoolSize());
		forkJoinPool.awaitQuiescence(100, TimeUnit.SECONDS);
		System.out.println("size is " + forkJoinPool.getPoolSize());
	}
	
	public String getTaskResult(ForkJoinPool forkJoinPool) throws InterruptedException, ExecutionException {
		ForkJoinTask<String> result = forkJoinPool.submit(() -> {
			TimeUnit.SECONDS.sleep(1);
			return getTaskResult(forkJoinPool);
		});
		String r = result.get();
		System.err.println(r);
		return r;
	}

}
