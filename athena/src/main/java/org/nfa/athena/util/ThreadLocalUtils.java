package org.nfa.athena.util;

import java.util.Random;

public class ThreadLocalUtils {

	private ThreadLocalUtils() {
		super();
	}

	public static final InheritableThreadLocal<String> inherit = new InheritableThreadLocal<String>() {
		private Random ran = new Random();

		@Override
		protected String initialValue() {
			return String.valueOf(ran.nextInt());
		}
	};

	public static final ThreadLocal<String> mainThreadLocal = new ThreadLocal<>();

}
