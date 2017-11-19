package org.nfa.athena;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class TestCountDownLatch {

	@Test
	public void test() throws InterruptedException {

		final int N = 5;
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch finishLatch = new CountDownLatch(N);
		final Random r = new Random();

		for (int i = 0; i < N; ++i) {
			new Thread(() -> {
				try {
					startLatch.await();
					System.out.println(Thread.currentThread().getName() + " starts!");
					Thread.sleep(r.nextInt(5000));
					System.out.println(Thread.currentThread().getName() + " done!");
					finishLatch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}

		System.out.println("All workers start!");
		startLatch.countDown();
		finishLatch.await();
		System.out.println("All workers done!");

	}

}
