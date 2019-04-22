package org.nfa.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

//@Controller
public class HomeController {

	@Autowired
	private TokenStore tokenStore;
//	@Value("${security.oauth2.client.client-id}")
	private String clientId = "magnet-client";

	@RequestMapping("/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	@RequestMapping("/")
	@ResponseBody
	public String home() {
		return "Home Page";
	}

	@RequestMapping("/loginFailed")
	@ResponseBody
	public String loginFailed() {
		return "Login Failed Page";
	}

	@RequestMapping("/logoutSuccess")
	@ResponseBody
	public String logout() {
		return "Logout Success Page";
	}

	@PostMapping("/logout/current")
	public boolean logoutCurrent() {
//		return AuthUtils.getCurrentOAuth2AuthenticationDetails().map(OAuth2AuthenticationDetails::getTokenValue)
//				.map(tokenStore::readAccessToken).map(this::remove).orElse(false);
		return false;
	}

	@PostMapping("/logout/user")
	public boolean logoutUser(@RequestParam("username") String username) {
		boolean removed = false;
		for (OAuth2AccessToken accessToken : tokenStore.findTokensByClientIdAndUserName(clientId, username)) {
			if (remove(accessToken)) {
				removed = true;
			}
		}
		return removed;
	}

	private boolean remove(OAuth2AccessToken accessToken) {
		if (accessToken == null) {
			return false;
		}
		if (accessToken.getRefreshToken() != null) {
			tokenStore.removeRefreshToken(accessToken.getRefreshToken());
		}
		tokenStore.removeAccessToken(accessToken);
		return true;
	}

}
