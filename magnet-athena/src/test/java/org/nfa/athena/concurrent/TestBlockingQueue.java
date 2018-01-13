package org.nfa.athena.concurrent;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.nfa.athena.User;

public class TestBlockingQueue {

	private static final Random RAN = new Random();
	private static final int CAPACITY = 3;
	private static final int THREAD_TOTAL = 10;

	@Test
	public void testLinkedBlockingQueue() throws InterruptedException {
		BlockingQueue<User> queue = new LinkedBlockingQueue<User>(CAPACITY);
		testBlockingQueue(queue);
	}

	@Test
	public void testSynchronousQueue() throws InterruptedException {
		BlockingQueue<User> queue = new SynchronousQueue<User>();
		testBlockingQueue(queue);
	}

	private void testBlockingQueue(BlockingQueue<User> queue) throws InterruptedException {
		for (int i = 0; i < THREAD_TOTAL; i++) {
			new Thread(() -> {
				try {
					System.out.println(Thread.currentThread().toString() + " start put");
					queue.put(new User(Thread.currentThread().toString(), Thread.currentThread().getPriority()));
					System.out.println(Thread.currentThread().toString() + " put one user");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}

		TimeUnit.SECONDS.sleep(5L);

		for (int i = 0; i < THREAD_TOTAL; i++) {
			new Thread(() -> {
				try {
					System.out.println(Thread.currentThread().toString() + " start take");
					TimeUnit.MILLISECONDS.sleep(RAN.nextInt(1000));
					System.out.println(Thread.currentThread().toString() + " take one user " + queue.take().getName());
					TimeUnit.MILLISECONDS.sleep(RAN.nextInt(1000));
					System.out.println(Thread.currentThread().toString() + " finish take");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}

		TimeUnit.SECONDS.sleep(5L);

	}

}
