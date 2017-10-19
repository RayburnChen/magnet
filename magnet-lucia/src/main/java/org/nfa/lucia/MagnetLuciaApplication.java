package org.nfa.lucia;

import org.nfa.athena.AthenaClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = AthenaClient.class, defaultConfiguration = FeignClientsConfiguration.class)
@EnableSwagger2
@EnableOAuth2Sso
//@EnableBinding(MagnetSink.class)
public class MagnetLuciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnetLuciaApplication.class, args);
	}
	
//	@StreamListener(MagnetSink.INPUT)
//	public void processEvent(User user) {
//		System.out.println("Get magnet event:" + user.toString());
//	}
//	
//	@StreamListener(MagnetSink.BUS)
//	public void processEvent(String event) {
//		System.out.println("Get bus event:" + event);
//	}

}
