package org.nfa.atropos;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class TestReactorThread {
	
	// 可以这样简单理解，对于中间过程的 Mono/Flux，subscribe 阶段是订阅上一个 Mono/Flux；而对于源 Mono/Flux，则是要执行 Subscriber.onSubscribe(Subscription s) 方法。
	// LambdaSubscriber.subscribe -> FluxMapFuseable.subscribe -> FluxFilterFuseable.subscribe -> FluxArray.subscribe
	// FluxArray.subscribe -> FilterFuseableSubscriber.onSubscribe -> MapFuseableSubscriber.onSubscribe -> LambdaSubscriber.onSubscribe
	// LambdaSubscriber.onSubscribe -> MapFuseableSubscriber.request -> FilterFuseableSubscriber.request -> ArraySubscription.request
	// ArraySubscription.request -> FilterFuseableSubscriber.onNext -> MapFuseableSubscriber.onNext -> LambdaSubscriber.onNext -> consumer.accept
	
	@Test
	public void testSubscribe() {
		Flux.just("tom", "jack", "allen") // FluxArray.ArraySubscription
			.filter(s -> s.length() > 3) // FluxFilterFuseable.FilterFuseableSubscriber
			.map(s -> s.concat("@qq.com")) // FluxMapFuseable.MapFuseableSubscriber
			.subscribe(System.out::println); // LambdaSubscriber
	}
	
	// 两者的区别在于影响范围。publishOn 影响在其之后的 operator 执行的线程池，而 subscribeOn 则会从源头影响整个执行过程。
	// 所以，publishOn 的影响范围和它的位置有关，而 subscribeOn 的影响范围则和位置无关。
	// 默认情况下， 操作链使用的线程是调用subscribe()的线程

	@Test
	public void testPublishOn() throws InterruptedException {
		Flux.just("tom")
				.map(s -> {
					System.out.println("[map] Thread name: " + Thread.currentThread().getName());
					return s.concat("@mail.com");
				})
				.publishOn(Schedulers.newElastic("thread-publishOn"))
				.filter(s -> {
					System.out.println("[filter] Thread name: " + Thread.currentThread().getName());
					return s.startsWith("t");
				})
				.subscribeOn(Schedulers.newElastic("thread-subscribeOn"))
				.subscribe(s -> {
					System.out.println("[subscribe] Thread name: " + Thread.currentThread().getName());
					System.out.println(s);
				});
		TimeUnit.SECONDS.sleep(1L);
	}

}
