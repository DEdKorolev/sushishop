package com.example.sushishop.controllers;

import com.example.sushishop.domain.User;
import com.example.sushishop.dto.UserDto;
import com.example.sushishop.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public String userList(Model model){
		System.out.println("Вызван метод userList");
		model.addAttribute("users", userService.getAll());
		return "userList";
	}

	@GetMapping("/new")
	public String newUser(Model model){
		System.out.println("Вызван метод newUser. Переход на форму регистрации.");
		model.addAttribute("user", new UserDto());
		return "user";
	}

	@PostMapping("/new")
	public String saveUser(UserDto dto, Model model, Principal principal){
		System.out.println("Вызван метод saveUser. Производится сохранение пользователя");
		if(userService.save(dto)) {
			System.out.println(dto.getRole());
			if (principal == null) {
				return "redirect:/login";
			}
			if (dto.getRole().toString() == "ADMIN") {
				return "redirect:/users";
			}
		} else {
			model.addAttribute("user", dto);
			return "user";
		}
		return null;
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/profile")
	// Класс Principal - авторизованный юзер с т.з. springsec
	public String profileUser(Model model, Principal principal){
		System.out.println("Вызван метод profileUser. Переход на страницу профиля пользователя.");
		if(principal == null){
			throw new RuntimeException("Вы не авторизованы"); // Вывод сообщения, если юзер не авторизован
		}
		User user = userService.findByName(principal.getName()); // Поиск пользователя по имени

		// Если имя найдено строится DTO
		UserDto dto = UserDto.builder()
				.id(user.getId())
				.username(user.getName())
				.email(user.getEmail())
				.role(user.getRole())
				.build();
		model.addAttribute("user", dto);
		return "profile";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/profile")
	public String updateProfileUser(UserDto dto, Model model, Principal principal){
		System.out.println("Вызван метод updateProfileUser");
		if(principal == null
				|| !Objects.equals(principal.getName(), dto.getUsername())){
			// Вывод сообщения, если имя не совпадает с DTO, чтобы пользователь не менял имя
			throw new RuntimeException("Вы не авторизованы");
		}

		if(dto.getPassword() != null
				&& !dto.getPassword().isEmpty()
				&& !Objects.equals(dto.getPassword(), dto.getMatchingPassword())){
			model.addAttribute("user", dto);
			return "profile";
		}

		userService.updateProfile(dto);
		return "redirect:/users/profile";
	}

//	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/{id}/delete")
	public String deleteUser(@PathVariable Long id) {
		System.out.println("Вызван метод deleteUser");
		userService.delete(id);
		return "redirect:/users";
	}
}
