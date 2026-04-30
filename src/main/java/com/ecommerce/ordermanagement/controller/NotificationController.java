package com.ecommerce.ordermanagement.controller;

import com.ecommerce.ordermanagement.service.EmailNotificationService;
import com.ecommerce.ordermanagement.service.SMSNotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    // Constructor Injection (RECOMMENDED)
    private final EmailNotificationService emailNotificationService;
    private final SMSNotificationService smsNotificationService;

    public NotificationController(EmailNotificationService emailNotificationService, SMSNotificationService smsNotificationService) {
        this.emailNotificationService = emailNotificationService;
        this.smsNotificationService = smsNotificationService;
        System.out.println("NotificationController : Constructor called with dependencies");
    }
    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String message){
        emailNotificationService.sendEmailNotification(message);
        return" Email Notification sent: "+message;
    }
    @GetMapping("/send-sms")
    public String sendSMS(@RequestParam String message){
        smsNotificationService.sendSMSNotification(message);
        return" SMS Notification sent: "+message;
    }
    @GetMapping("send-both")
    public String sendBoth(@RequestParam String message){
        emailNotificationService.sendEmailNotification(message);
        smsNotificationService.sendSMSNotification(message);
        return "Both Notification sent : "+ message;
    }

}
