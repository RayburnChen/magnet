package org.nfa.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import zipkin2.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
// @EnableZipkinStreamServer
public class MagnetZipkinApplication {

	// https://cloud.spring.io/spring-cloud-static/Dalston.SR4/multi/multi__span_data_as_messages.html#_zipkin_consumer

	public static void main(String[] args) {
		SpringApplication.run(MagnetZipkinApplication.class, args);
	}

}
