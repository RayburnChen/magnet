package org.nfa.atropos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AtroposController {

	private static final Logger log = LoggerFactory.getLogger(AtroposController.class);

	@RequestMapping("/welcome")
	public String welcome(@RequestHeader HttpHeaders headers) {
		log.info("Headers: {}", headers);
		return "Welcome";
	}

}
