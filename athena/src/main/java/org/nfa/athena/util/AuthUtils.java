package org.nfa.athena.util;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public interface AuthUtils {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getDecodedDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof OAuth2Authentication) {
			Object details = ((OAuth2Authentication) authentication).getDetails();
			if (details instanceof OAuth2AuthenticationDetails) {
				return (Map<String, Object>) ((OAuth2AuthenticationDetails) details).getDecodedDetails();
			}
		}
		return null;
	}

}
