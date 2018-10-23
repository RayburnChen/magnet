package org.nfa.athena.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.StampedLock;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStampedLock {

	private static final Logger log = LoggerFactory.getLogger(TestStampedLock.class);

	private double x, y;
	private final StampedLock sl = new StampedLock();

	// 0000,0000,0001 RUNIT readLock increment
	// 0000,1000,0000 WBIT writeLock increment
	// 0000,1111,1111 ABITS check writeLock ((s = state) & ABITS) == 0L
	// 1111,0111,1111 SBITS validate OptimisticRead (stamp & SBITS) == (state & SBITS)

	@Test
	public void testStampedLock() {
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			futures.add(CompletableFuture.runAsync(() -> oneStep()));
		}
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
	}

	private void oneStep() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		moveIfAtOrigin(random.nextDouble(), random.nextDouble());
		move(random.nextDouble(), random.nextDouble());
		distanceFromOrigin();
	}

	private void move(double deltaX, double deltaY) { // an exclusively locked method
		// 0001,0000,0000 initial state
		// 0001,1000,0000 writeLock
		// 0010,0000,0000 unlockWrite
		long stamp = sl.writeLock();
		try {
			x += deltaX;
			y += deltaY;
		} finally {
			sl.unlockWrite(stamp);
		}
	}

	private double distanceFromOrigin() { // A read-only method
		// 0001,0000,0000 initial state
		// 0001,0000,0000 tryOptimisticRead
		// 0001,0000,0001 readLock
		// 0001,0000,0000 unlockRead
		long stamp = sl.tryOptimisticRead();
		double currentX = x, currentY = y;
		if (!sl.validate(stamp)) {
			stamp = sl.readLock();
			try {
				currentX = x;
				currentY = y;
			} finally {
				sl.unlockRead(stamp);
			}
		}
		double dis = Math.sqrt(currentX * currentX + currentY * currentY);
		log.info("name {} distance {}", Thread.currentThread().toString(), dis);
		return dis;
	}

	private void moveIfAtOrigin(double newX, double newY) { // upgrade
		// Could instead start with optimistic, not read mode
		// 0001,0000,0000 initial state
		// 0001,0000,0001 readLock
		// 0001,1000,0000 tryConvertToWriteLock
		// 0010,0000,0000 unlock

		// 0001,0000,0000 initial state
		// 0001,0000,0001 readLock
		// 0000,0000,0000 tryConvertToWriteLock failed
		// 0001,0000,0000 unlockRead
		// 0001,1000,0000 writeLock
		// 0010,0000,0000 unlock
		long stamp = sl.readLock();
		try {
			while (x == 0.0 && y == 0.0) {
				long ws = sl.tryConvertToWriteLock(stamp);
				if (ws != 0L) {
					stamp = ws;
					x = newX;
					y = newY;
					break;
				} else {
					sl.unlockRead(stamp);
					stamp = sl.writeLock();
				}
			}
		} finally {
			sl.unlock(stamp);
		}
	}
}
