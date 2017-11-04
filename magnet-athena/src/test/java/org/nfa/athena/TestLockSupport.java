package org.nfa.athena;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;

public class TestLockSupport {

	@Test
	public void testLockSupportPark() throws InterruptedException {
		String blocker = "Block reason";
		Thread thread = new Thread(() -> {
			System.out.println("Start work");
			LockSupport.park(blocker);
			System.out.println("Finish work");
			System.out.println("Thread.interrupted: " + Thread.interrupted());
		});
		thread.start();
		TimeUnit.SECONDS.sleep(2L);
		LockSupport.unpark(thread);// thread.interrupt();
		thread.join();
	}

}
