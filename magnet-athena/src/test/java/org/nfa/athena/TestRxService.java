package org.nfa.athena;

import org.junit.Test;
import org.nfa.athena.service.RxService;

public class TestRxService {
	
	
	@Test
	public void testRxService() {
		RxService s = new RxService();
		s.start();
	}

}
