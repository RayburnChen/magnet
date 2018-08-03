package org.nfa.common;

public interface Priority {

	/**
	 * only for handling quick and urgent tasks
	 */
	public static final int HIGHEST = 10;

	/**
	 * for chief business logic which need to wait and get result
	 */
	public static final int CHIEF = 20;

	/**
	 * for asynchronous logic which do not need result
	 */
	public static final int MINOR = 30;

	/**
	 * for tasks which is not serious
	 */
	public static final int LOWEST = 40;

}
