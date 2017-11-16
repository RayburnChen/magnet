package org.nfa.athena;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class TestReentrantLock {

	private int result = 0;
	private int count = 5;
	private ReentrantLock lock = new ReentrantLock();
	
	

	@Test
	public void testReentrantLock() {
		List<Thread> list = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			Thread th = new Thread(() -> {
				System.out.println(Thread.currentThread().toString() + " start");
				lock.lock();
				System.out.println(Thread.currentThread().toString() + " lock");
				try {
					int num = result;
					try {
						TimeUnit.SECONDS.sleep(40);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					result = num + 1;
				} finally {
					lock.unlock();
					System.out.println(Thread.currentThread().toString() + " unlock");
				}
			});
			list.add(th);
		}

		list.forEach(t -> t.start());

		list.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		System.out.println("TestReentrantLock.testReentrantLock() " + result);

	}
	
}
