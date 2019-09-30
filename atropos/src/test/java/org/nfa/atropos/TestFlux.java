package org.nfa.atropos;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

}
