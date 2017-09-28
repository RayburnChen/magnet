package org.nfa.athena;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class TestCountDownLatch {

	@Test
	public void test() {
		final int N = 5;
		final CountDownLatch startSignal = new CountDownLatch(1);
		final CountDownLatch doneSignal = new CountDownLatch(N);
		final Random r = new Random();
		for (int i = 0; i < N; ++i)
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						startSignal.await();
						System.out.println(Thread.currentThread().getName() + " starts!");
						Thread.sleep(r.nextInt(5000));
						System.out.println(Thread.currentThread().getName() + " done!");
					} catch (Exception e) {
					}
					doneSignal.countDown();
				}
			}).start();
		System.out.println("All workers start!");
		startSignal.countDown();
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("All workers done!");
	}

}
