package org.nfa.atropos.controller;

import org.nfa.atropos.model.thulac.SegItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AtroposController {

	private static final Logger log = LoggerFactory.getLogger(AtroposController.class);

	@GetMapping("/welcome")
	public SegItem welcome(@RequestHeader HttpHeaders headers) {
		log.info("Headers: {}", headers);
		return new SegItem("welcome", String.valueOf(headers.getHost()));
	}

	@GetMapping("/exception")
	public void exception() {
		throw new RuntimeException("AtroposController exception Assert.isNull");
	}

}
