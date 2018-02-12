package org.nfa.athena.common;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.Test;
import org.nfa.athena.User;

public class TestSort {

	private static final Comparator<User> SORT = Comparator.comparing(User::getName,
			Comparator.nullsFirst(Comparator.naturalOrder()));
	private static final Comparator<User> SORT_ERROR = Comparator.nullsFirst(Comparator.comparing(User::getName));

	@Test
	public void test() {
		Arrays.asList(new User("user01", 0), new User("user02", 0), new User(null, 0)).stream().sorted(SORT)
				.forEach(System.out::print);
	}

	@Test
	public void testError() {
		Arrays.asList(new User("user01", 0), new User("user02", 0), new User(null, 0)).stream().sorted(SORT_ERROR)
				.forEach(System.out::print);
	}

}
