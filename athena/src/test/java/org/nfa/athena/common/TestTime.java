package org.nfa.athena.common;

import java.time.Instant;
import java.time.ZoneId;

import org.junit.Test;

public class TestTime {

	@Test
	public void test() {
		ZoneId zoneId = ZoneId.of("Asia/Shanghai");
		System.out.println(Instant.now().atZone(zoneId));
	}

}
