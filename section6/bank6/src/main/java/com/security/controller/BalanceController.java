package com.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    @GetMapping("/balance")
    public String balance(){
        System.out.println("balance controller");
        return "Welcome to balance page";
    }
}
