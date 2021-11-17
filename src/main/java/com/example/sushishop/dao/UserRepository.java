package com.example.sushishop.dao;

import com.example.sushishop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Работает с сущностью User. Long - тип данных первичного ключа
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByName(String name);

}
