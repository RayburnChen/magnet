package org.nfa.athena;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class TestCyclicBarrier {

	@Test
	public void test() {
		final int N = 5;
		final AtomicInteger counter = new AtomicInteger(N * 3);
		final CyclicBarrier barrier = new CyclicBarrier(N);
		final Random r = new Random();
		for (int i = 0; i < N; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (counter.getAndDecrement() > 0) {
						System.out.println(Thread.currentThread().getName() + " starts!");
						try {
							Thread.sleep(r.nextInt(2000));
							System.out.println(Thread.currentThread().getName() + " done!");
							barrier.await();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
		do {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (counter.get() > 0);
	}

}
