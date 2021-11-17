package com.example.sushishop.controllers;

import com.example.sushishop.domain.User;
import com.example.sushishop.dto.UserDto;
import com.example.sushishop.service.UserService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Controller
//@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public String userList(Model model){
		model.addAttribute("users", userService.getAll());
		return "userList";
	}

	@GetMapping("/newClient")
	public String newClient(Model model){
		System.out.println("Called method newClient");
		model.addAttribute("user", new UserDto());
		return "user";
	}

	@GetMapping("/newUser")
	public String newUser(Model model){
		System.out.println("Called method newUser");
		model.addAttribute("user", new UserDto());
		return "user";
	}

	@PostAuthorize("isAuthenticated() and #username == authentication.principal.username")
	@GetMapping("/{name}/roles")
	@ResponseBody
	public String getRoles(@PathVariable("name") String username){
		System.out.println("Called method getRoles");
		User byName = userService.findByName(username);
		return byName.getRole().name();
	}

	@PostMapping("/newClient")
	public String saveClient(UserDto dto, Model model){
		if(userService.saveClient(dto)){
			return "redirect:/login";
		}
		else {
			model.addAttribute("user", dto);
			return "user";
		}
	}

	@PostMapping("/newUser")
	public String saveUser(UserDto dto, Model model){
		if(userService.saveUser(dto)){
			return "redirect:/users";
		}
		else {
			model.addAttribute("user", dto);
			return "user";
		}
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/profile")
	// Класс Principal - авторизованный юзер с т.з. springsec
	public String profileUser(Model model, Principal principal){
		if(principal == null){
			throw new RuntimeException("Вы не авторизованы"); // Вывод сообщения, если юзер не авторизован
		}
		User user = userService.findByName(principal.getName()); // Поиск юзера по имени

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
		userService.delete(id);
		return "redirect:/users";
	}
}
