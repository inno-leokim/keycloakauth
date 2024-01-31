package com.brand13.keycloakauth.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @GetMapping(path = "/")
    public String index() {
        return "external";
    }
    
    @GetMapping(path = "/customers")
    public String customers(Principal principal) {
        return "customers";
    }
}
