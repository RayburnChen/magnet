package org.nfa.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class MagnetGatewayApplication {

	// http://localhost:8080/actuator/gateway/routes
	// http://localhost:8080/MAGNET-ATHENA/greeting/oneUser
	// http://localhost:8080/athena/greeting/oneUser

	public static void main(String[] args) {
		SpringApplication.run(MagnetGatewayApplication.class, args);
	}

}
