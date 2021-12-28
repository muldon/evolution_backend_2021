package com.github.eduoliveiradev.evolution.controller;

import com.github.eduoliveiradev.evolution.dto.LoginRequest;
import com.github.eduoliveiradev.evolution.service.ClienteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final ClienteService clienteService;

    public LoginController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public String login(@Validated @RequestBody LoginRequest loginRequest) {
        return clienteService.getToken(loginRequest);
    }
}
