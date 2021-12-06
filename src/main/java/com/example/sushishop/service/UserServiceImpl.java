package com.example.sushishop.service;

import com.example.sushishop.dao.UserRepository;
import com.example.sushishop.domain.Role;
import com.example.sushishop.domain.User;
import com.example.sushishop.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<UserDto> getAll() {
		System.out.println("Вызван метод getAll");
		return userRepository.findAll().stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public boolean save(UserDto userDto) {
		System.out.println("Вызван метод save");
		if(!Objects.equals(userDto.getPassword(), userDto.getMatchingPassword())){
			System.out.println("Пароль и подтверждение пароля не совпадают");
			throw new RuntimeException("Пароль и подтверждение пароля не совпадают");
		}

		if(userRepository.findFirstByName(userDto.getUsername()) != null){
			System.out.println("Пользователь с таким именем уже существует");
			throw new RuntimeException("Пользователь с таким именем уже существует. Введите другое имя.");
		}

		if(userRepository.findFirstByEmail(userDto.getEmail()) != null){
			System.out.println("Пользователь с таким Email уже существует");
			throw new RuntimeException("Пользователь с таким Email уже существует. Для регистрации выберите другой Email.");
		}
		// Создание нового пользователя
		User user = User.builder()
			.id(userDto.getId())
			.name(userDto.getUsername())
			.password(passwordEncoder.encode(userDto.getPassword()))
			.email(userDto.getEmail())
			.address(userDto.getAddress())
			.role(userDto.getRole())
			.build();
		if (userDto.getRole() == null) {
			user.setRole(Role.CLIENT);
		}
		userRepository.save(user);
		System.out.println("Пользователь сохранён в репозиторий");
		return true;
	}

	@Override
	public User findByName(String name) {
		System.out.println("Вызван метод findByName. Поиск пользователя по имени в репозитории");
		return userRepository.findFirstByName(name);
	}

	@Override
	@Transactional
	public void updateProfile(UserDto dto) {
		System.out.println("Вызван метод updateProfile");
		User savedUser = userRepository.findFirstByName(dto.getUsername());
		if(savedUser == null){
			throw new RuntimeException("Пользователь с именем " + dto.getUsername() + " не найден.");
		}

		boolean changed = false;
		// Проверка на совпадение текущего пароля с новым
		if(dto.getPassword() != null && !dto.getPassword().isEmpty()){
			savedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
			changed = true;
		}
		// Проверка на совпадение текущего email с новым
		if(!Objects.equals(dto.getEmail(), savedUser.getEmail())){
			if(userRepository.findFirstByEmail(dto.getEmail()) != null){
				throw new RuntimeException("Пользователь с таким Email уже существует. Выберите другой Email.");
			}
			savedUser.setEmail(dto.getEmail());
			changed = true;
		}

		// Проверка на совпадение текущего адреса с новым
		if(!Objects.equals(dto.getAddress(), savedUser.getAddress())){
			savedUser.setAddress(dto.getAddress());
			changed = true;
		}

		// Произвести сохранение данных о пользователе, если были произведены изменения
		if(changed){
			userRepository.save(savedUser);
		}
	}

	@Override
	public void save(User user) {
		System.out.println("Вызван метод save1");
		userRepository.save(user);
	}

	@Override
	public void delete(Long id) {
		System.out.println("Вызван метод delete. Пользователь удалён из репозитория.");
		userRepository.deleteById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Вызван метод loadUserByUsername. Загрузка данных пользователя по его имени.");
		User user = userRepository.findFirstByName(username);
		if(user == null){
			System.out.println("Пользователь с именем " + username + " не найден.");
			throw new UsernameNotFoundException("Пользователь с именем " + username + " не найден.");
		}

		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority(user.getRole().name()));

		return new org.springframework.security.core.userdetails.User(
				user.getName(),
				user.getPassword(),
				roles);
	}

	private UserDto toDto(User user){
		System.out.println("Вызван метод toDto");
		return UserDto.builder()
				.id(user.getId())
				.username(user.getName())
				.email(user.getEmail())
				.address(user.getAddress())
				.role(user.getRole())
				.build();
	}

}
