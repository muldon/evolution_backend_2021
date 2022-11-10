package com.github.eduoliveiradev.evolution.controller;

import com.github.eduoliveiradev.evolution.dto.ClienteCreationRequest;
import com.github.eduoliveiradev.evolution.service.ClienteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public UUID criaCliente(@Validated @RequestBody ClienteCreationRequest clienteDTO) {
        return clienteService.save(clienteDTO);
    }
    
    
    
    
}
