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
					startLatch.await();// wait for startLatch.countDown() and then start
					System.out.println(Thread.currentThread().getName() + " starts!");
					Thread.sleep(r.nextInt(5000));
					System.out.println(Thread.currentThread().getName() + " done!");
					finishLatch.countDown();// finishLatch countDown -1
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}

		System.out.println("All workers start!");
		startLatch.countDown();// trigger all threads to start
		finishLatch.await();// wait for finishLatch.countDown N times
		System.out.println("All workers done!");

	}

}
