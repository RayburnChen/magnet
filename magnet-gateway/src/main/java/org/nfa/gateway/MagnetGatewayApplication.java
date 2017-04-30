package org.nfa.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableHystrix
@EnableHystrixDashboard
public class MagnetGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnetGatewayApplication.class, args);
	}
}
