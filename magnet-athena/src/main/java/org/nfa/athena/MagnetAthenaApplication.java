package org.nfa.athena;

import org.nfa.panel.config.EnableMagnetService;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableMagnetService
@EnableCaching
public class MagnetAthenaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnetAthenaApplication.class, args);
	}

}
