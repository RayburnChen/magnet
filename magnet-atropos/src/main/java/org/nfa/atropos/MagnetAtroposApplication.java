package org.nfa.atropos;

import org.nfa.panel.config.EnableMagnetService;
import org.springframework.boot.SpringApplication;

@EnableMagnetService
public class MagnetAtroposApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnetAtroposApplication.class, args);
	}

}
