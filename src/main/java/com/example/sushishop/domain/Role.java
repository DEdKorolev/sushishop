package com.example.sushishop.domain;
import lombok.*;

public enum Role {
    CLIENT, MANAGER, ADMIN;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Widget {
        private String name;
        private Role role;
    }
}

