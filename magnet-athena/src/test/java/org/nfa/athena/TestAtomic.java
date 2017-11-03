package org.nfa.athena;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class TestAtomic {

	private static final int THREAD_COUNT = 5;

	@Test
	public void testAtomicReference() {

		User initUser = new User();
		initUser.setName("owen");
		initUser.setAge(1);
		AtomicReference<User> atomicUser = new AtomicReference<>(initUser);

		System.out.println("AtomicReference User is " + atomicUser.get().toString());

		List<Thread> list = new ArrayList<>(THREAD_COUNT);
		for (int i = 0; i < THREAD_COUNT; i++) {
			list.add(new Thread(() -> {

				User result = atomicUser.updateAndGet(expect -> {
					User update = new User();
					update.setName(Thread.currentThread().toString());
					update.setAge(expect.getAge() + 1);
					try {
						TimeUnit.MILLISECONDS.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return update;
				});

				System.out.println("AtomicReference User is " + result.toString());

			}));
		}

		list.forEach(t -> t.start());

		list.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		System.out.println("AtomicReference Done ");

	}

}
