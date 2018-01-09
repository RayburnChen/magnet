package org.nfa.athena.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import org.junit.Test;
import org.nfa.athena.User;

public class TestBlockingQueue {

	@Test
	public void testLinkedBlockingQueue() {
		BlockingQueue<User> queue = new LinkedBlockingQueue<User>();
	}

	@Test
	public void testSynchronousQueue() {
		BlockingQueue<User> queue = new SynchronousQueue<User>();
	}

}
