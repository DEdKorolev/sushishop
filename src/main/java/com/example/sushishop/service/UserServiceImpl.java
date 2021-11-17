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
		return userRepository.findAll().stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public boolean saveClient(UserDto userDto) {
		if(!Objects.equals(userDto.getPassword(), userDto.getMatchingPassword())){
			throw new RuntimeException("Password is not equal");
		}
		User user = User.builder()
				.id(userDto.getId())
				.name(userDto.getUsername())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.email(userDto.getEmail())
				.role(Role.CLIENT)
				.build();
		userRepository.save(user);
		return true;
	}

	@Override
	@Transactional
	public boolean saveUser(UserDto userDto) {
		if(!Objects.equals(userDto.getPassword(), userDto.getMatchingPassword())){
			throw new RuntimeException("Password is not equal");
		}
		User user = User.builder()
				.id(userDto.getId())
				.name(userDto.getUsername())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.email(userDto.getEmail())
				.role((Role) userDto.getRole())
				.build();
		userRepository.save(user);
		return true;
	}

	@Override
	public User findByName(String name) {
		return userRepository.findFirstByName(name);
	}

	@Override
	@Transactional
	public void updateProfile(UserDto dto) {
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
			savedUser.setEmail(dto.getEmail());
			changed = true;
		}
		// Произвести сохранение данных о пользователе, если были произведены изменения
		if(changed){
			userRepository.save(savedUser);
		}
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findFirstByName(username);
		if(user == null){
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
		return UserDto.builder()
				.id(user.getId())
				.username(user.getName())
				.email(user.getEmail())
				.role(user.getRole())
				.build();
	}

}
