package org.nfa.atropos;

import org.nfa.base.service.config.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableDiscoveryClient
@EnableCircuitBreaker
@Import({ GlobalExceptionHandler.class })
public class MagnetAtroposApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnetAtroposApplication.class, args);
	}

}
