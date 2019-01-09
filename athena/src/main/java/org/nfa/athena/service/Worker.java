package org.nfa.athena.service;

import java.util.Random;

import org.nfa.athena.util.ThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(Worker.class);

	private static final ThreadLocal<String> semaphore = new ThreadLocal<String>() {

		private Random ran = new Random();

		@Override
		protected String initialValue() {
			return String.valueOf(ran.nextInt());
		}

	};

	@Override
	public void run() {
		log.info("{}-Id-{} semaphore {}", Thread.currentThread().getName(), Thread.currentThread().getId(), semaphore.get());
		log.info("{}-Id-{} inherit {}", Thread.currentThread().getName(), Thread.currentThread().getId(), ThreadLocalUtils.inherit.get());
		log.info("{}-Id-{} mainThreadLocal {}", Thread.currentThread().getName(), Thread.currentThread().getId(), ThreadLocalUtils.mainThreadLocal.get());
	}

}
