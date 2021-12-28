package com.github.eduoliveiradev.evolution.service;

import com.github.eduoliveiradev.evolution.dto.ClienteCreationRequest;
import com.github.eduoliveiradev.evolution.dto.LoginRequest;
import com.github.eduoliveiradev.evolution.entity.Cliente;
import com.github.eduoliveiradev.evolution.repository.ClienteRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ClienteService implements UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clienteRepository.findByEmail(username)
                .map(cliente -> new User(cliente.email(), cliente.senha(), List.of(new SimpleGrantedAuthority("ROLE_USER")) ))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public String getToken(LoginRequest loginRequest) {
       return clienteRepository.findByEmail(loginRequest.email())
               .filter(cliente -> passwordEncoder.matches(loginRequest.password(), cliente.senha()))
               .map(this::createToken)
                .orElseThrow(() -> new UsernameNotFoundException(loginRequest.email()));

    }

    private String createToken(Cliente cliente) {
        Instant now = Instant.now();
        long expiry = 36000L;
        // @formatter:off
        String scope = "ROLE_USER";
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(cliente.email())
                .claim("scope", scope)
                .build();
        // @formatter:on
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
