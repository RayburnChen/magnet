package org.nfa.athena.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Subscription;

public class RxService {

	private static Logger log = LoggerFactory.getLogger(RxService.class);

	public void start() {
		Subscription subscription = Observable.from(Arrays.asList("RxJava01", "RxJava02", "RxJava03"))
				.subscribe(
						n -> log.info("Observer.onNext {}", n),
						e -> log.error("Observer.onError", e), 
						() -> log.info("Observer.onCompleted"));
		subscription.unsubscribe();
	}

}
