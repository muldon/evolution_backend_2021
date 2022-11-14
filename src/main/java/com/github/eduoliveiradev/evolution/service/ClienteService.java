package com.github.eduoliveiradev.evolution.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.github.eduoliveiradev.evolution.dto.ClienteCreationRequest;
import com.github.eduoliveiradev.evolution.dto.LoginRequest;
import com.github.eduoliveiradev.evolution.entity.Cliente;
import com.github.eduoliveiradev.evolution.repository.ClienteRepository;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder encoder;
     
    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder, JwtEncoder encoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.encoder = encoder;
    }

    public UUID save(ClienteCreationRequest clienteDTO) {
        Cliente cliente = transform(clienteDTO);
        final Cliente save = clienteRepository.save(cliente);
        return save.uuid();
    }

    private Cliente transform(ClienteCreationRequest clienteDTO) {
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

    public String getToken(LoginRequest loginRequest) {
       return clienteRepository.findByEmail(loginRequest.email())
               .filter(cliente -> passwordEncoder.matches(loginRequest.password(), cliente.senha()))
               .map(this::createToken)
                .orElseThrow(() -> new UsernameNotFoundException(loginRequest.email()));
       
       
    }
     
   
    private String createToken(Cliente loginRequest) {
        Instant now = Instant.now();
        //long expiry = 36000L; // 10 horas
        long expiry = 300L; // 300 seconds
        String scope = "ROLE_USER";
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(loginRequest.uuid().toString())
                .claim("scope", scope)
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    
    
    
}
