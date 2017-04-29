package org.nfa.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MagnetSink {
	
	String INPUT = "magnet";
	
	String BUS = "springCloudBus";
	
	@Input(MagnetSink.INPUT)
	SubscribableChannel input();
	
	@Input(MagnetSink.BUS)
	SubscribableChannel bus();
	
}
