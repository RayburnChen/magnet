package org.nfa.athena.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import org.junit.Test;
import org.nfa.athena.User;

public class TestAtomic {

	

	@Test
	public void testAtomicReference() {
		
		int THREAD_COUNT = 5;

		AtomicReference<User> atomicUser = new AtomicReference<>(new User("owen", 1));

		System.out.println("AtomicReference User is " + atomicUser.get().toString());

		List<Thread> list = new ArrayList<>(THREAD_COUNT);
		for (int i = 0; i < THREAD_COUNT; i++) {
			list.add(new Thread(() -> {

				User result = atomicUser.updateAndGet(expect -> {
					User update = new User(Thread.currentThread().toString(), expect.getAge() + 1);
					try {
						TimeUnit.MILLISECONDS.sleep(100);
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

	private static final AtomicReferenceFieldUpdater<TestAtomic, User> UPDATER = AtomicReferenceFieldUpdater
			.newUpdater(TestAtomic.class, User.class, "initUser");
	
	private volatile User initUser = new User("owen", 1);

	@Test
	public void testAtomicReferenceFieldUpdater() {
		
		int THREAD_COUNT = 5;

		System.out.println("AtomicReferenceFieldUpdater User is " + initUser.toString());

		List<Thread> list = new ArrayList<>(THREAD_COUNT);
		for (int i = 0; i < THREAD_COUNT; i++) {
			list.add(new Thread(() -> {

				User result = UPDATER.updateAndGet(this, expect -> {
					User update = new User(Thread.currentThread().toString(), expect.getAge() + 1);
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return update;
				});

				System.out.println("AtomicReferenceFieldUpdater User is " + result.toString());

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

		System.out.println("AtomicReferenceFieldUpdater Done ");

	}

}
