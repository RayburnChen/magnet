package org.nfa.athena;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestSemaphore {
	
	// Semaphore
	
	// Semaphore.acquire()
	// AbstractQueuedSynchronizer.acquireSharedInterruptibly(1)
	// Semaphore.NonfairSync.nonfairTryAcquireShared(1)
	// remaining = available - acquires
	// if remaining >= 0 and CAS pass then thread continue
	// if remaining < 0 then add thread to queue and park AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(1)
	
	// Semaphore.release()
	// AbstractQueuedSynchronizer.releaseShared(1)
	// Add back semaphore and CAS the state
	// Unpark all Nodes in the queue and they will try to acquire semaphore

	private final int count = 6;

	@Test
	public void testSemaphore() {

		Semaphore semaphore = new Semaphore(3);

		List<Thread> list = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			list.add(new Thread(() -> {
				try {
					semaphore.acquire();
					System.out.println(Thread.currentThread().toString() + " start");
					TimeUnit.SECONDS.sleep(5);
					System.out.println(Thread.currentThread().toString() + " done");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					semaphore.release();
				}
			}));
		}

		list.forEach(t -> t.start());

		list.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

	}

}
