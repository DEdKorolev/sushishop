package com.example.sushishop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class MainController {

	@RequestMapping({"","/"})
	public String index(Model model, HttpSession httpSession){
		System.out.println("Вызван метод index. Переход на главную страницу.");
		if(httpSession.getAttribute("myID") == null){
			String uuid = UUID.randomUUID().toString();
			httpSession.setAttribute("myID", uuid);
			System.out.println("Generated UUID -> " + uuid);
		}
		model.addAttribute("uuid", httpSession.getAttribute("myID"));

		return "index";
	}

	@RequestMapping("/login")
	public String login(){
		System.out.println("Вызван метод login. Переход на форму авторизации.");
		return "login";
	}

	@RequestMapping("/login-error")
	public String loginError(Model model){
		System.out.println("Вызван метод loginError. Неверно введено имя пользователя или пароль");
		model.addAttribute("loginError", true);
		return "login";
	}

}
