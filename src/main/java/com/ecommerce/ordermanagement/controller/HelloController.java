package com.ecommerce.ordermanagement.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from E-commerce System!";
    }
    @GetMapping("/status")
    public String status() {
        return "System is running on MS SQL Server";
    }

}
