package org.nfa.atropos;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TestFlux {

	@Test
	public void zipWith() {
		Flux.just("a", "b").zipWith(Flux.just("c", "d", "e")).subscribe(System.out::println);
		Flux.just("a", "b").zipWith(Flux.just("c", "d", "e"), (s1, s2) -> String.format("%s-%s", s1, s2)).subscribe(System.out::println);
	}

	@Test
	public void retryWhen() {
		Flux.<String>error(new IllegalArgumentException()).retryWhen(companion -> companion.zipWith(Flux.range(1, 4), (error, index) -> {
			if (index < 4)
				return index;
			else
				throw Exceptions.propagate(error);
		})).subscribe();
	}

	@Test
	public void retryWhenWithDelay() throws InterruptedException {
		Flux.<String>error(new IllegalArgumentException()).retryWhen(companion -> companion

				.doOnNext(s -> System.out.println(s + " at " + LocalTime.now()))

				.zipWith(Flux.range(1, 4), (error, index) -> {
					if (index < 4)
						return index;
					else
						throw Exceptions.propagate(error);
				})

				.flatMap(index -> Mono.delay(Duration.ofMillis(index * 100)))

				.doOnNext(s -> System.out.println("retried at " + LocalTime.now()))

		).subscribe();
		TimeUnit.SECONDS.sleep(3L);
	}

	@Test
	public void testDelayElements() {
		// delayElements 有些操作符本身会需要调度器来进行多线程的处理，当你不明确指定调度器的时候，那些操作符会自行使用内置的单例调度器来执行。例如，Flux.delayElements(Duration)使用的是 Schedulers.parallel()调度器对象
		Flux.range(0, 10).delayElements(Duration.ofMillis(10)).log().blockLast();
	}
	
	@Test
	public void testParallelFluxPublishOn() throws InterruptedException {
		Flux.range(1, 10).publishOn(Schedulers.parallel()).log().subscribe();
		TimeUnit.MILLISECONDS.sleep(10);
	}
	
	@Test
	public void testParallelFluxRunOn() throws InterruptedException {
		Flux.range(1, 10).parallel(2).runOn(Schedulers.parallel()).log().subscribe();
		TimeUnit.MILLISECONDS.sleep(10);
	}

}
