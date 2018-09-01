package org.nfa.atropos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableDiscoveryClient
@EnableCircuitBreaker
public class MagnetAtroposApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnetAtroposApplication.class, args);
	}

}
