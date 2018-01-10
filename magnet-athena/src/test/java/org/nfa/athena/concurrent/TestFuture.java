package org.nfa.athena.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class TestFuture {

	// size = 1
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(1);

	@Test
	public void test() {
		String result = null;
		try {
			result = EXECUTOR.submit(new MainThread()).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		try {
			Thread.sleep(30000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class MainThread implements Callable<String> {

		@Override
		public String call() throws Exception {
			
			// case 1 normal
			EXECUTOR.submit(new MinorThread());
			
			// case 2 dead lock
			// EXECUTOR.submit(new MinorThread()).get();
			
			// case 3 throw time out exception
			// EXECUTOR.submit(new MinorThread()).get(3, TimeUnit.SECONDS);
			
			return "MainThread Done";
		}

	}

	class MinorThread implements Callable<String> {

		@Override
		public String call() throws Exception {
			Thread.sleep(3000L);
			System.out.println("MinorThread Done");
			return "MinorThread Result";
		}

	}

}
