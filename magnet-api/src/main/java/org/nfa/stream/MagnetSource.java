package org.nfa.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MagnetSource {

	String OUTPUT = "magnet";

	@Output(MagnetSource.OUTPUT)
	MessageChannel output();
	
}
