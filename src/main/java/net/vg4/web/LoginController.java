package net.vg4.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping("loginForm")
	String loginForm() {
		logger.info("dispatch loginForm");
		return "loginForm";
	}

}
