package org.nfa.base.service.impl;

import java.util.Map;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

	// auth server encode token
	@Override
	public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		token.getAdditionalInformation().put("author", "owen");
		token.getAdditionalInformation().put("organization", "nfa");
		return super.convertAccessToken(token, authentication);
	}

	// resource server decode token
	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		OAuth2Authentication oAuth2Authentication = super.extractAuthentication(map);
		oAuth2Authentication.setDetails(map);
		return oAuth2Authentication;
	}

}
