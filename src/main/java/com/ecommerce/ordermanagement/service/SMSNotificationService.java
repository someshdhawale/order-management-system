package com.ecommerce.ordermanagement.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class SMSNotificationService {

    @PostConstruct
    public void init(){
        System.out.println("SMSNotificationService : Initializing SMS service");
    }

    public  void sendSMSNotification(String message){
        System.out.println("SMS : " + message);
    }
}
