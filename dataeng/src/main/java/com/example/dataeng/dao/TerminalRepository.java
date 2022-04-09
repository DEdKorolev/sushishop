package com.example.dataeng.dao;

import com.example.dataeng.domain.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {
}
