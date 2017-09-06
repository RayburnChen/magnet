package org.nfa.athena;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class TestThreadPool {
	
	private static final ExecutorService POOL = Executors.newFixedThreadPool(10);

	@Test
	public void test() {
//		POOL.submit(task);
	}

}
