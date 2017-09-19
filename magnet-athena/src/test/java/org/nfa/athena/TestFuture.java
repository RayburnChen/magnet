package org.nfa.athena;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class TestFuture {

	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
			EXECUTOR.submit(new MinorThread());
			return "MainThread Done";
		}

	}

	class MinorThread implements Callable<String> {

		@Override
		public String call() throws Exception {
			Thread.sleep(3000L);
			System.out.println("MinorThread Done");
			return null;
		}

	}

}
