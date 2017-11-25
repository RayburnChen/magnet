package org.nfa.athena;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class TestCyclicBarrier {

	@Test
	public void test() throws InterruptedException {
		final Random r = new Random();
		final int N = 3;
		final AtomicInteger counter = new AtomicInteger(N * 3);
		final CyclicBarrier barrier = new CyclicBarrier(N);
		List<Thread> list = new ArrayList<>(N);
		for (int i = 0; i < N; i++) {
			list.add(new Thread(() -> {
				while (counter.getAndDecrement() > 0) {
					try {
						System.out.println(Thread.currentThread().getName() + " starts!");
						TimeUnit.MILLISECONDS.sleep(r.nextInt(3000));
						System.out.println(Thread.currentThread().getName() + " done!");
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				}

			}));
		}

		System.out.println("TestCyclicBarrier.test() Start");
		list.forEach(t -> t.start());
		list.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		System.out.println("TestCyclicBarrier.test() Done");

	}

}
