package org.nfa.athena.service;

import java.util.Random;

import org.nfa.athena.TestThreadLocal;

public class Worker implements Runnable {

	private static final ThreadLocal<String> semaphore = new ThreadLocal<String>() {

		@Override
		protected String initialValue() {
			Random ran = new Random();
			return String.valueOf(ran.nextInt());
		}

	};

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + "-Id-" + Thread.currentThread().getId() + " semaphore " + semaphore.get());
		System.out.println(Thread.currentThread().getName() + "-Id-" + Thread.currentThread().getId() + " inherit " + TestThreadLocal.inherit.get());
		System.out.println(Thread.currentThread().getName() + "-Id-" + Thread.currentThread().getId() + " mainThreadLocal " + TestThreadLocal.mainThreadLocal.get());
	}

}
