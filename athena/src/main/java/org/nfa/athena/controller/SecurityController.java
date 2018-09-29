package org.nfa.athena.controller;

import java.util.Map;

import org.nfa.athena.util.AuthUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityController {

	@GetMapping("/self")
	public Map<String, Object> self() {
		return AuthUtils.getDecodedDetails();
	}

}
