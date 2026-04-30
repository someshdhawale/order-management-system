package com.ecommerce.ordermanagement.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;


@Service
public class EmailNotificationService {

    @PostConstruct
    public void init() {
        System.out.println("EmailNotificationService : Initializing email service ");
    }
    public  void sendEmailNotification(String message){
        System.out.println("EMAIL "+message);
    }
    @PreDestroy
    public void destroy() {
        System.out.println("EmailNotificationService : Destroying email service");
    }
}
