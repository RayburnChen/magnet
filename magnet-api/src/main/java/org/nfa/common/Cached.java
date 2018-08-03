package org.nfa.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cached {

	/**
	 * <p>
	 * cache name
	 * <p>
	 * essential
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * <p>
	 * true : fresh cache immediately
	 * <p>
	 * false : fresh cache asynchronous
	 * 
	 * @return
	 */
	boolean fresh() default false;

	/**
	 * <p>
	 * true : fresh all cache with same cacheName
	 * <p>
	 * false : not fresh all cache
	 * 
	 * @return
	 */
	boolean freshAll() default false;

	/**
	 * <p>
	 * true : lazy load
	 * <p>
	 * false : eager load
	 * 
	 * @return
	 */
	boolean lazy() default false;

	/**
	 * <p>
	 * asyncTimeout() >= 0 : asynchronous execution time limit
	 * <p>
	 * asyncTimeout() < 0 : asynchronous never timeout
	 * <p>
	 * use TimeUnit.SECONDS
	 * 
	 * @return
	 */
	long asyncTimeout() default 30L;

	/**
	 * <p>
	 * timeToLive() >= 0 : cache time to live
	 * <p>
	 * timeToLive() < 0 : cache do not vanish
	 * <p>
	 * use TimeUnit.SECONDS
	 * <p>
	 * default 30 days
	 * 
	 * @return
	 */
	long timeToLive() default 2592000L;

}
