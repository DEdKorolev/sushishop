// Контроллер для вылавливания исключений во всем приложении

package com.example.sushishop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorControllerAdvice {

	@ExceptionHandler(Exception.class) // Ограничено Exception
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Статус ответа: ошибка на сервере
	public String exception(Exception exception, Model model){
		String errorMessage = (exception != null ? exception.getMessage() : "Неизвестная ошибка");
		model.addAttribute("errorMessage", errorMessage);
		return "error";
	}
}