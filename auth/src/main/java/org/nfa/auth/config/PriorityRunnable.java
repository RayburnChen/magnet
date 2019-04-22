package org.nfa.auth.config;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class PriorityRunnable implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(PriorityRunnable.class);

	private final Runnable runnable;
	private SecurityContext originalContext;
	private final SecurityContext delegateContext;
	private final int priority;

	public PriorityRunnable(Runnable runnable, int priority) {
		super();
		this.runnable = runnable;
		this.priority = priority;
		this.delegateContext = SecurityContextHolder.getContext();
	}

	public int getPriority() {
		return priority;
	}

	public SecurityContext getOriginalContext() {
		return originalContext;
	}

	@Override
	public void run() {
		this.originalContext = SecurityContextHolder.getContext();
		try {
			SecurityContextHolder.setContext(delegateContext);
			runnable.run();
		} catch (Exception e) {
			log.debug(ExceptionUtils.getStackTrace(e));
		} finally {
			SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
			if (emptyContext.equals(originalContext)) {
				SecurityContextHolder.clearContext();
			} else {
				SecurityContextHolder.setContext(originalContext);
			}
			this.originalContext = null;
		}
	}

	@Override
	public String toString() {
		return runnable.toString();
	}

}
