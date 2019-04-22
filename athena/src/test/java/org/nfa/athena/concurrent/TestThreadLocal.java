package org.nfa.athena.concurrent;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.nfa.athena.service.Worker;
import org.nfa.athena.util.ThreadLocalUtils;

public class TestThreadLocal {

	@Test
	public void test() {
		ThreadLocalUtils.inherit.set("owen");
		ThreadLocalUtils.mainThreadLocal.set("mainThreadLocal");
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(new Worker());
			t.start();
		}
		try {
			TimeUnit.SECONDS.sleep(2L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
