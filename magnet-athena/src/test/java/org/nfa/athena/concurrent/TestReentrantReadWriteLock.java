package org.nfa.athena.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Test;

public class TestReentrantReadWriteLock {

	@Test
	public void test() {
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		lock.readLock();
		lock.writeLock();
	}

}
