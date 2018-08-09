package org.nfa.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class MagnetGatewayApplication {

	// http://localhost:8080/actuator/gateway/routes
	// http://localhost:8080/MAGNET-ATHENA/greeting/oneUser
	// http://localhost:8080/athena/greeting/oneUser

	public static void main(String[] args) {
		SpringApplication.run(MagnetGatewayApplication.class, args);
	}

	@Bean
	public DiscoveryClientRouteDefinitionLocator discoveryClientRouteLocator(DiscoveryClient discoveryClient, DiscoveryLocatorProperties discoveryLocatorProperties) {
		return new DiscoveryClientRouteDefinitionLocator(discoveryClient, discoveryLocatorProperties);
	}

}
