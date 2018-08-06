package org.nfa.athena;

import org.nfa.panel.config.EnableMagnetService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableMagnetService
@EnableCaching
public class MagnetAthenaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnetAthenaApplication.class, args);
	}

}
