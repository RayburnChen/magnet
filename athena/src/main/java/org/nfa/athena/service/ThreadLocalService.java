package org.nfa.athena.service;

import java.util.Random;

public class ThreadLocalService {

	public static final InheritableThreadLocal<String> inherit = new InheritableThreadLocal<String>() {
		@Override
		protected String initialValue() {
			Random ran = new Random();
			return String.valueOf(ran.nextInt());
		}
	};
	public static final ThreadLocal<String> mainThreadLocal = new ThreadLocal<String>();

}
