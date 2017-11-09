package org.nfa.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping("/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	@RequestMapping("/home")
	@ResponseBody
	public String home() {
		return "Home Page";
	}

	@RequestMapping("/loginFailed")
	@ResponseBody
	public String loginFailed() {
		return "Login Failed Page";
	}

	@RequestMapping("/logout")
	@ResponseBody
	public String logout() {
		return "Logout Page";
	}

}
