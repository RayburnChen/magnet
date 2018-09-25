package org.nfa.athena.concurrent;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestExchanger {

	private static final Random RAN = new Random();
	private static final int THREAD_TOTAL = 10;

	@Test
	public void testExchanger() throws InterruptedException {
		// swap elements within pairs
		// just like exchange name card
		// http://blog.csdn.net/luoyuyou/article/details/30259877
		Exchanger<String> exchanger = new Exchanger<String>();
		for (int i = 0; i < THREAD_TOTAL; i++) {
			new Thread(() -> {
				try {
					System.out.println(Thread.currentThread().toString() + " start exchange");
					TimeUnit.MILLISECONDS.sleep(RAN.nextInt(1000));
					String feedback = exchanger.exchange(Thread.currentThread().toString());
					System.out.println(Thread.currentThread().toString() + " feedback " + feedback);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}

		TimeUnit.SECONDS.sleep(5L);
		
	}

}
