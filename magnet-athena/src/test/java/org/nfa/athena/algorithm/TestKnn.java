package org.nfa.athena.algorithm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

import org.junit.Test;
import org.nfa.athena.model.User;

public class TestKnn {

	private static final int K = 3;

	@Test
	public void test() {
//		for (int i = 0; i < 1000000000; i++) {
		System.out.println("Tag:" + kttGetTag(new User(new BigDecimal(String.valueOf(100000)), "unknown")) + " for " + 100000);
//		}
	}

	private String kttGetTag(User user) {
		KnnList knnList = new KnnList(K, user);
		List<User> sample = getSample();
		List<User> result = knnList.goThrough(sample.stream());
		return getMost(result);
	}

	private List<User> getSample() {
		List<User> sample = new ArrayList<>();
		sample.add(new User(new BigDecimal("1"), "begger"));
		sample.add(new User(new BigDecimal("10"), "begger"));
		sample.add(new User(new BigDecimal("100"), "poor"));
		sample.add(new User(new BigDecimal("1000"), "poor"));
		sample.add(new User(new BigDecimal("10000"), "medium"));
		sample.add(new User(new BigDecimal("100000"), "medium"));
		sample.add(new User(new BigDecimal("1000000"), "rich"));
		sample.add(new User(new BigDecimal("10000000"), "rich"));
		sample.add(new User(new BigDecimal("100000000"), "powerful"));
		sample.add(new User(new BigDecimal("1000000000"), "powerful"));
		return sample;
	}

	private String getMost(List<User> users) {
		Map<String, Integer> map = new HashMap<>();
		users.forEach(u -> {
			if (map.containsKey(u.getTag())) {
				Integer inc = map.remove(u.getTag()) + 1;
				map.put(u.getTag(), inc);
			} else {
				map.put(u.getTag(), 1);
			}
		});
		Comparator<Entry<String, Integer>> comp = Comparator.comparingInt(Entry::getValue);
		PriorityQueue<Entry<String, Integer>> queue = new PriorityQueue<Entry<String, Integer>>(comp.reversed());
		map.entrySet().forEach(queue::offer);
		return queue.peek().getKey();
	}

	public static class KnnList {

		public KnnList(int size, User user) {
			super();
			ToDoubleFunction<User> func = u -> u.distance(user);
			this.queue = new PriorityQueue<User>(size + 1, Comparator.comparingDouble(func));
			this.size = size;
		}

		private final PriorityQueue<User> queue;
		private final int size;

		public List<User> goThrough(Stream<User> stream) {
			stream.forEach(queue::offer);
			List<User> result = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				result.add(queue.poll());
			}
			return result;
		}

	}

}
