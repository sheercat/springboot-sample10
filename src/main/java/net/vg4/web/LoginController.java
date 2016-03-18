package net.vg4.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController {
	@RequestMapping("loginForm")
	String loginForm() {
		log.info("dispatch loginForm");
		return "loginForm";
	}

}
