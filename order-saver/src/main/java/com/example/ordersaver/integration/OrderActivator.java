package com.example.ordersaver.integration;

import com.example.ordersaver.domain.Order;
import com.example.ordersaver.service.OrderService;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderActivator {

    private final OrderService orderService;

    public OrderActivator(OrderService orderService) {
        this.orderService = orderService;
    }

    @ServiceActivator(inputChannel = "ordersChannel")
    public void listenOrdersChannel(@Payload Order payload, @Headers Map<String,Object> headers){
        System.out.println("Получен заказ: " + payload);
        orderService.save(payload);
    }
}
