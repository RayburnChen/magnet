package org.nfa.athena.concurrent;

import org.junit.Test;
import org.nfa.athena.service.ThreadLocalService;
import org.nfa.athena.service.Worker;

public class TestThreadLocal {

	@Test
	public void test() {
		ThreadLocalService.inherit.set("owen");
		ThreadLocalService.mainThreadLocal.set("mainThreadLocal");
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(new Worker());
			t.start();
		}
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
