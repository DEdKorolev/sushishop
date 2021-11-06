package com.example.sushishop.service;

import com.example.sushishop.domain.Order;

public interface OrderService {
    void saveOrder(Order order);

    Order getOrder(Long id);
}
