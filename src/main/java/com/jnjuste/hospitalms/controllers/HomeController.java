package com.jnjuste.hospitalms.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/api/home")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to the Hospital Management System! This is a test to know that both FrontEnd and SpringBoot Backend are connected");
        return response;
    }
}

