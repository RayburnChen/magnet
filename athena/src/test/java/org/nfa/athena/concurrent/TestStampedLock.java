package org.nfa.athena.concurrent;

import java.util.concurrent.locks.StampedLock;

import org.junit.Test;

public class TestStampedLock {
	
	@Test
	public void testExchanger() {
		StampedLock stampedLock = new StampedLock();
		stampedLock.readLock();
		stampedLock.writeLock();
	}
	
}
