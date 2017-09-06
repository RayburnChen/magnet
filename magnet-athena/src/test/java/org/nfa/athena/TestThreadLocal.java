package org.nfa.athena;

import org.junit.Test;
import org.nfa.athena.service.Worker;

public class TestThreadLocal {
	
	public static final InheritableThreadLocal<String> inherit = new InheritableThreadLocal<String>();
	public static final ThreadLocal<String> mainThreadLocal = new ThreadLocal<String>();

	@Test
	public void test() {
		inherit.set("owen");
		mainThreadLocal.set("mainThreadLocal");
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
