package org.nfa.athena;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestCountDownLatch {

	@Test
	public void test() throws InterruptedException {
		
//				CountDownLatch(1) AQS Node [thread,waitStatus]
//
//				count 1
//
//				t1 add
//				t1 park
//				[null,-1], [t1,0]
//
//				t2 add
//				t2 park
//				[null,-1], [t1,-1], [t2,0]
//
//				t3 add
//				t3 park
//				[null,-1], [t1,-1], [t2,-1], [t3,0]
//
//				count 0
//
//				[null,0], [t1,-1], [t2,-1], [t3,0]
//
//				t1 unpark
//				[null,0], [t2,-1], [t3,0]
//				t1 run
//
//				t2 unpark
//				[null,0], [t3,0]
//				t2 run
//
//				t3 unpark
//				[null,0]
//				t3 run
		

		final int N = 3;
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch finishLatch = new CountDownLatch(N);
		final Random r = new Random();

		for (int i = 0; i < N; ++i) {
			new Thread(() -> {
				try {
					startLatch.await();
					System.out.println(Thread.currentThread().getName() + " starts!");
					TimeUnit.MILLISECONDS.sleep(r.nextInt(5000));
					System.out.println(Thread.currentThread().getName() + " done!");
					finishLatch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}

		TimeUnit.SECONDS.sleep(1);
		System.out.println("All workers start!");
		startLatch.countDown();
		finishLatch.await();
		System.out.println("All workers done!");

	}
	

	// CountDownLatch.await()
	// When CountDownLatch count reach zero, it stop decrease.
	// If CountDownLatch count == 0 then CountDownLatch.await() will pass immediately
	// If CountDownLatch count > 0 then CountDownLatch.await() will add Node to AQS and park thread
	
	// CountDownLatch.countDown()
	// If CountDownLatch count > 0 then CountDownLatch.countDown() will decrease count by 1
	// If CountDownLatch count == 0 already then CountDownLatch.countDown() will do nothing
	// When CountDownLatch.countDown() only one thread decrease and transition to zero will cause AQS.doReleaseShared()
	
	// AQS.doReleaseShared()
	// Only once and signals successor and ensures propagation
	// Set head Node waitStatus -1 -> 0 and unpark successor Node
	// Recursive set new head and propagate
	

}
