package com.example.sushishop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // создает геттеры и сеттеры
@NoArgsConstructor // создаст конструктор без параметров
@AllArgsConstructor // создает конструктор со всеми параметрами
@Builder // создает все конструкторы с одним параметром
public class UserDto {
	private Long id;
	private String username;
	private String password;
	private String matchingPassword;
	private String email;
	private Enum role;
}

