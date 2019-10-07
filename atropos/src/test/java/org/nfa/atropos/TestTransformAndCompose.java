package org.nfa.atropos;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.junit.Test;

import reactor.core.publisher.Flux;

public class TestTransformAndCompose {

	@Test
	public void testCompose() {
        AtomicInteger ai = new AtomicInteger();
        Function<Flux<String>, Flux<String>> filterAndMap = f -> {
            if (ai.incrementAndGet() == 1) {
            	// Subscriber 1
                return f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);
            }
            // Subscriber 2
            return f.filter(color -> !color.equals("purple"))
                    .map(String::toUpperCase);
        };

        Flux<String> flux =
                Flux.just("blue", "green", "orange", "purple")
                        .doOnNext(System.out::println)
                        .compose(filterAndMap);
        // Defer the transformation of this Flux in order to generate a target Flux type. 
        // A transformation will occur for each Subscriber
        flux.subscribe(d -> System.out.println("Subscriber 1 get: " + d));
        flux.subscribe(d -> System.out.println("Subscriber 2 get: " + d));
    }
	
//	blue
//	Subscriber 1 get: BLUE
//	green
//	Subscriber 1 get: GREEN
//	orange
//	purple
//	Subscriber 1 get: PURPLE
//	blue
//	Subscriber 2 get: BLUE
//	green
//	Subscriber 2 get: GREEN
//	orange
//	Subscriber 2 get: ORANGE
//	purple
	
	
	@Test
	public void testTransform() {
        AtomicInteger ai = new AtomicInteger();
        Function<Flux<String>, Flux<String>> filterAndMap = f -> {
            if (ai.incrementAndGet() == 1) {
            	// Subscriber 1
            	// Subscriber 2
                return f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);
            }
            return f.filter(color -> !color.equals("purple"))
                    .map(String::toUpperCase);
        };

        Flux<String> flux =
                Flux.just("blue", "green", "orange", "purple")
                        .doOnNext(System.out::println)
                        .transform(filterAndMap);
        // Transform this Flux in order to generate a target Flux. 
        // Unlike compose(Function), the provided function is executed as part of assembly. 
        flux.subscribe(d -> System.out.println("Subscriber 1 get: " + d));
        flux.subscribe(d -> System.out.println("Subscriber 2 get: " + d));
    }
	
//	blue
//	Subscriber 1 get: BLUE
//	green
//	Subscriber 1 get: GREEN
//	orange
//	purple
//	Subscriber 1 get: PURPLE
//	blue
//	Subscriber 2 get: BLUE
//	green
//	Subscriber 2 get: GREEN
//	orange
//	purple
//	Subscriber 2 get: PURPLE
	
}
