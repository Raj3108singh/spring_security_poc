package com.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    @GetMapping("/profile")
    public String profile(){
        System.out.println("profile controller");
        return "Welcome to profile page";
    }
}
