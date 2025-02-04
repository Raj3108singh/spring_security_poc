package com.security.controller;

import com.security.model.Customer;
import com.security.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    //Here we are exposing an api which will allow users to create creds through the API
    //For this work we are creating a custom

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {

        try {

            String hashPassword = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPassword);
           Customer savedCustomer =  customerRepository.save(customer);
            if(savedCustomer.getId() > 0){
                return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Registered");
            }


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception occured" + e.getMessage());
        }
        return null;
    }
}
