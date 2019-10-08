package org.nfa.atropos.literx;

import org.nfa.atropos.literx.domain.User;
import org.nfa.atropos.literx.repository.BlockingRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Learn how to call blocking code from Reactive one with adapted concurrency strategy for
 * blocking code that produces or receives data.
 *
 * For those who know RxJava:
 *  - RxJava subscribeOn = Reactor subscribeOn
 *  - RxJava observeOn = Reactor publishOn
 *  - RxJava Schedulers.io <==> Reactor Schedulers.elastic
 *
 * @author Sebastien Deleuze
 * @see Flux#subscribeOn(Scheduler)
 * @see Flux#publishOn(Scheduler)
 * @see Schedulers
 */
public class Part11BlockingToReactive {
	
	// 两者的区别在于影响范围。publishOn 影响在其之后的 operator 执行的线程池，而 subscribeOn 则会从源头影响整个执行过程。
	// 所以，publishOn 的影响范围和它的位置有关，而 subscribeOn 的影响范围则和位置无关。

//========================================================================================

	// TODO Create a Flux for reading all users from the blocking repository deferred until the flux is subscribed, and run it with an elastic scheduler
	Flux<User> blockingRepositoryToFlux(BlockingRepository<User> repository) {
		return Flux.defer(() -> Flux.fromIterable(repository.findAll()))
				.subscribeOn(Schedulers.elastic());
	}

//========================================================================================

	// TODO Insert users contained in the Flux parameter in the blocking repository using an elastic scheduler and return a Mono<Void> that signal the end of the operation
	Mono<Void> fluxToBlockingRepository(Flux<User> flux, BlockingRepository<User> repository) {
		return flux.publishOn(Schedulers.elastic())
				.doOnNext(repository::save)
				.then();
	}

}
