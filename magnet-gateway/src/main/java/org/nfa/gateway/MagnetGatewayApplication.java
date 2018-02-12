package org.nfa.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class MagnetGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnetGatewayApplication.class, args);
	}

	// http://localhost:8080/magnet-gateway/v1/athena/magnet-athena/greeting/oneUser
	// http://localhost:8080/magnet-gateway/v1/lucia/magnet-lucia/athenaUser

}
