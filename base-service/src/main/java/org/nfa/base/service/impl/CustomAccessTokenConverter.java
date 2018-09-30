package org.nfa.base.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

public class CustomAccessTokenConverter implements AccessTokenConverter {

	private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

	public AccessTokenConverter getTokenConverter() {
		return tokenConverter;
	}

	public void setTokenConverter(AccessTokenConverter tokenConverter) {
		this.tokenConverter = tokenConverter;
	}

	// auth server encode token
	@Override
	public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		token.getAdditionalInformation().put("author", "owen");
		token.getAdditionalInformation().put("organization", "nfa");
		return tokenConverter.convertAccessToken(token, authentication);
	}

	// resource server decode token
	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		OAuth2Authentication oAuth2Authentication = tokenConverter.extractAuthentication(map);
		Map<String, Object> info = new HashMap<String, Object>(map);
		info.remove(EXP);
		info.remove(AUD);
		info.remove(CLIENT_ID);
		info.remove(SCOPE);
		info.remove(JTI);
		oAuth2Authentication.setDetails(info);
		return oAuth2Authentication;
	}

	@Override
	public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
		return tokenConverter.extractAccessToken(value, map);
	}

}
