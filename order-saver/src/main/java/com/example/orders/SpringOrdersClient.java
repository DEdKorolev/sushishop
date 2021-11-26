package com.example.orders;

import com.example.ordersaver.domain.Order;
import com.example.ordersaver.domain.OrderDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SpringOrdersClient {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringOrdersClient.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8084"));
        ConfigurableApplicationContext context = app.run(args);

        OrderIntegrationConfig config = context.getBean("orderIntegrationConfig", OrderIntegrationConfig.class);

        DirectChannel ordersChannel = config.getOrdersChannel();

        Order order = new Order();
        order.setOrderId(12345);
        order.setUsername("user");
        order.setAddress("address");

        List<OrderDetails> list = new ArrayList<>();
        list.add(OrderDetails.builder().product("Филадельфия").price(350.0).amount(2.0).sum(700.0).build());
        list.add(OrderDetails.builder().product("Суши с креветкой").price(50.0).amount(2.0).sum(100.0).build());

        order.setDetails(list);

        Message<Order> message = MessageBuilder.
                withPayload(order)
                .setHeader("Content-type", "application/json")
                .build();

        ordersChannel.send(message);

    }
}
