package org.nfa.athena.concurrent;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

public class TestCompletableFuture {

	@Test
	public void testStaticMethod() {
		for (int i = 0; i < 100; i++) {
			CompletableFuture.supplyAsync(() -> Thread.currentThread().toString()).thenAcceptAsync(lastThread -> {
				String threadString = Thread.currentThread().toString();
				System.out.println("executing " + lastThread + " --> " + threadString);
			});
		}
	}

}
