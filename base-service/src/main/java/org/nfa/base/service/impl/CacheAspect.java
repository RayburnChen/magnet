package org.nfa.base.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.nfa.base.model.ApplicationException;
import org.nfa.base.model.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;

import com.netflix.hystrix.exception.HystrixRuntimeException;

@Aspect
public class CacheAspect {

	private static final Logger logger = LoggerFactory.getLogger(CacheAspect.class);

	// private final CacheSupplier cacheSupplier;
	//
	// public CacheAspect(CacheSupplier cacheSupplier) {
	// super();
	// this.cacheSupplier = cacheSupplier;
	// }

	@Around("@annotation(cached)")
	public Object around(ProceedingJoinPoint joinPoint, Cached cached) throws Throwable {
		return searchCache(joinPoint, cached);
	}

	private Object searchCache(ProceedingJoinPoint joinPoint, Cached option) throws Throwable {

		String cacheKey = mergeKey(option, joinPoint);

		if (option.fresh()) {
			// fresh cache
			logger.debug("Cache fresh " + cacheKey);
			// cacheSupplier.delete(cacheKey);
			// cacheSupplier.deleteCacheKey(option.value(), cacheKey);
		}

		if (option.freshAll()) {
			// fresh cache
			logger.debug("Cache fresh all " + option.value());
			// cacheSupplier.deleteAll(option.value());
		}

		try {
			// Object resultFromCache = cacheSupplier.get(cacheKey);
			Object resultFromCache = null;
			if (null == resultFromCache && !option.lazy()) {
				// cache miss and lazy false, need to call service for result
				logger.trace("Cache miss " + cacheKey);
				Object fetch = joinPoint.proceed();
				AsyncService.build().add(Priority.HIGHEST, () -> setCache(fetch, option, cacheKey));
				return fetch;
			} else {
				// cache hit or lazy true, return immediately
				logger.trace("Cache hit " + cacheKey);
				AsyncService.build().add(Priority.LOWEST, () -> {
					// add asynchronous task to refresh cache
					try {
						Object fetch = joinPoint.proceed();
						setCache(fetch, option, cacheKey);
						return fetch;
					} catch (Throwable e) {
						throw new ApplicationException("Async Service execute JoinPoint failed " + e.toString(), 10023012, e);
					}
				});
				return resultFromCache;
			}
		} catch (ApplicationException | HystrixRuntimeException | RestClientException e) {
			logger.trace("CacheSupplier Exception", e);
			throw e;
		} catch (RuntimeException e) {
			logger.error("CacheSupplier Unavailable", e);
			return joinPoint.proceed();
		}

	}

	private void setCache(Object fetch, Cached option, String cacheKey) {
		if (null == fetch) {
			return;
		}
		if (option.timeToLive() < 0) {
			// cacheSupplier.set(cacheKey, fetch);
			// cacheSupplier.setCacheKey(option.value(), cacheKey);
		} else {
			// cacheSupplier.set(cacheKey, fetch, option.timeToLive());
			// cacheSupplier.setCacheKey(option.value(), cacheKey);
		}
	}

	private static String mergeKey(Cached option, ProceedingJoinPoint joinPoint) {
		return mergeKey(option.value(), joinPoint.getArgs());
	}

	public static String mergeKey(String cacheName, Object[] params) {
		Assert.notNull(cacheName, "CacheName must not be null");
		return cacheName = new String(cacheName) + ":" + Optional.ofNullable(params).map(Arrays::asList).orElseGet(ArrayList::new).toString();
	}

}
