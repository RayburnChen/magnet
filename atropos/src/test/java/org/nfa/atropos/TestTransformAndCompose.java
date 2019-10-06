package org.nfa.atropos;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.junit.Test;

import reactor.core.publisher.Flux;

public class TestTransformAndCompose {

	@Test
    public void testTransform() {
        Function<Flux<String>, Flux<String>> filterAndMap =
                f -> f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);

        Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .transform(filterAndMap)
                .subscribe(d -> System.out.println("Subscriber to Transformed MapAndFilter: "+d));
    }
	
	@Test
	public void testCompose() {
        AtomicInteger ai = new AtomicInteger();
        Function<Flux<String>, Flux<String>> filterAndMap = f -> {
            if (ai.incrementAndGet() == 1) {
                return f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);
            }
            return f.filter(color -> !color.equals("purple"))
                    .map(String::toUpperCase);
        };

        Flux<String> composedFlux =
                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                        .doOnNext(System.out::println)
                        .compose(filterAndMap);

        composedFlux.subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :" + d));
        composedFlux.subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: " + d));
    }
	
//	compose:
//	blue
//	Subscriber 1 to Composed MapAndFilter :BLUE
//	green
//	Subscriber 1 to Composed MapAndFilter :GREEN
//	orange
//	purple
//	Subscriber 1 to Composed MapAndFilter :PURPLE
//	blue
//	Subscriber 2 to Composed MapAndFilter: BLUE
//	green
//	Subscriber 2 to Composed MapAndFilter: GREEN
//	orange
//	Subscriber 2 to Composed MapAndFilter: ORANGE
//	purple
	
//	transform:
//	blue
//	Subscriber 1 to Composed MapAndFilter :BLUE
//	green
//	Subscriber 1 to Composed MapAndFilter :GREEN
//	orange
//	purple
//	Subscriber 1 to Composed MapAndFilter :PURPLE
//	blue
//	Subscriber 2 to Composed MapAndFilter: BLUE
//	green
//	Subscriber 2 to Composed MapAndFilter: GREEN
//	orange
//	purple
//	Subscriber 2 to Composed MapAndFilter: PURPLE
	
}
