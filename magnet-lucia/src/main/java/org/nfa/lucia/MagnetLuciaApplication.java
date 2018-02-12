package org.nfa.lucia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
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
