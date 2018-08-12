package org.nfa.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableCircuitBreaker
public class MagnetZuulApplication {

	// http://localhost:8080/actuator/routes
	// http://localhost:8080/athena/greeting/oneUser

	public static void main(String[] args) {
		SpringApplication.run(MagnetZuulApplication.class, args);
	}

}
