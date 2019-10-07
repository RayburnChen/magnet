package org.nfa.atropos.literx;

import org.nfa.atropos.literx.domain.User;
import org.nfa.atropos.literx.repository.ReactiveRepository;
import org.nfa.atropos.literx.repository.ReactiveUserRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Learn how to control the demand.
 *
 * @author Sebastien Deleuze
 */
public class Part06Request {

	ReactiveRepository<User> repository = new ReactiveUserRepository();

//========================================================================================

	// TODO Create a StepVerifier that initially requests all values and expect 4 values to be received
	StepVerifier requestAllExpectFour(Flux<User> flux) {
		return StepVerifier.create(flux)
				.expectNextCount(4L)
				.expectComplete();
	}

//========================================================================================

	// TODO Create a StepVerifier that initially requests 1 value and expects User.SKYLER then requests another value and expects User.JESSE.
	StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
		return StepVerifier.create(flux)
				.thenRequest(1L)
				.expectNext(User.SKYLER)
				.thenRequest(1L)
				.expectNext(User.JESSE)
				.thenCancel();
	}

//========================================================================================

	// TODO Return a Flux with all users stored in the repository that prints automatically logs for all Reactive Streams signals
	Flux<User> fluxWithLog() {
		return repository.findAll().log();
	}

//========================================================================================

	// TODO Return a Flux with all users stored in the repository that prints "Starring:" on subscribe, "firstname lastname" for all values and "The end!" on complete
	Flux<User> fluxWithDoOnPrintln() {
		return repository.findAll()
				.doOnSubscribe(sub -> System.out.println("Starring:"))
				.doOnNext(p -> System.out.println(p.getFirstname() + " " + p.getLastname()))
				.doOnComplete(() -> System.out.println("The end!"));
	}

}
