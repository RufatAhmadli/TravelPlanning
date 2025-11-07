package edu.az.example.web.travelplanning.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/notifications")
    public String sendMessage(String message){
        System.out.println("message : "+message);
        return message;
    }
}
