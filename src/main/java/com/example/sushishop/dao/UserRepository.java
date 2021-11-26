package com.example.sushishop.dao;

import com.example.sushishop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByName(String name);
    User findFirstByEmail(String name);
}
