package com.example.snowflake.controller;

import com.example.snowflake.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/snowflake")
    public String index() {
        return orderService.getIDBySnowFlake();
    }
}
