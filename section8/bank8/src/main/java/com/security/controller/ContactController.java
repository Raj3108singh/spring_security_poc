package com.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    @GetMapping("/contact")
    public String contact(){
        System.out.println("contact controller");
        return "Welcome to contact page";
    }
}
