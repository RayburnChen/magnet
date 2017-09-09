package org.nfa.athena.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CallAsyncService {
	
	private static Logger log = LoggerFactory.getLogger(CallAsyncService.class);
	
	@Async
	public void asyncMethod() {
		try {
			Thread.sleep(3000L);
			log.info("CallAsyncService.asyncMethod()");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
