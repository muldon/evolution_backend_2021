package com.github.eduoliveiradev.evolution.service;

import com.github.eduoliveiradev.evolution.dto.ClienteDTO;
import com.github.eduoliveiradev.evolution.entity.Cliente;
import com.github.eduoliveiradev.evolution.repository.ClienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UUID save(ClienteDTO clienteDTO) {
        Cliente cliente = transform(clienteDTO);
        final Cliente save = clienteRepository.save(cliente);
        return save.uuid();
    }

    private Cliente transform(ClienteDTO clienteDTO) {
        return new Cliente(
                null,
                clienteDTO.nome(),
                clienteDTO.email(),
                clienteDTO.cpf(),
                clienteDTO.rg(),
                clienteDTO.endereco(),
                clienteDTO.renda(),
                passwordEncoder.encode(clienteDTO.senha())
        );
    }
}
