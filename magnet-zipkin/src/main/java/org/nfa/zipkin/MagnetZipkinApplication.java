package org.nfa.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

@SpringBootApplication
@EnableZipkinStreamServer
public class MagnetZipkinApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnetZipkinApplication.class, args);
	}
}
