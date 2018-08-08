package org.nfa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class MagnetConfigApplication {

	// http://localhost:8888/magnet-athena/dev

	public static void main(String[] args) {
		SpringApplication.run(MagnetConfigApplication.class, args);
	}
}
