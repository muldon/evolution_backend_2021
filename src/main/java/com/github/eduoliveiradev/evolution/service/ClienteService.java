package com.github.eduoliveiradev.evolution.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.github.eduoliveiradev.evolution.dto.AccessToken;
import com.github.eduoliveiradev.evolution.dto.ClienteCreationRequest;
import com.github.eduoliveiradev.evolution.dto.LoginRequest;
import com.github.eduoliveiradev.evolution.dto.LoginRequest2;
import com.github.eduoliveiradev.evolution.entity.Cliente;
import com.github.eduoliveiradev.evolution.repository.ClienteRepository;
import com.github.eduoliveiradev.evolution.utils.OAuthUtils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder encoder;
    
    @Value("${tao.public.key}")
    String key;

    @Value("${tao.private.key}")
    String priv;

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
    
    
//    public String getToken(LoginRequest2 loginRequest2) {
////      return clienteRepository.findByEmail(loginRequest2.email())
////              //.filter(cliente -> passwordEncoder.matches(loginRequest.password(), cliente.senha()))
////              .map(this::createToken)
////               .orElseThrow(() -> new UsernameNotFoundException(loginRequest2.email()));
//
//      return createToken(loginRequest2);
//      
//      
//   }
    
    public AccessToken getToken2(LoginRequest2 loginRequest2) throws GeneralSecurityException, IOException {
        return createToken2(loginRequest2);

     }
    
    public String getToken3() throws GeneralSecurityException, IOException {
        return createToken3();

     }

    private AccessToken createToken2(LoginRequest2 loginRequest2) throws GeneralSecurityException, IOException {
        Instant now = Instant.now();
        //long expiry = 36000L; // 10 horas
        long expiry = 600L; // 10 min
        String scope = "https://purl.imsglobal.org/spec/lti-ags/scope/score";
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject("AAAA")
                .claim("scope", scope)
                .build();
        String accessToken = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        String tokenType = "bearer";
        //Long exp = 3l;
        AccessToken at = new AccessToken(accessToken, tokenType,expiry, scope);
        System.out.println("createToken2: "+accessToken);
        return at;
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
    
    private String createToken3() {
        Instant now = Instant.now();
        //long expiry = 36000L; // 10 horas
        long expiry = 300L; // 300 seconds
        String scope = "ROLE_USER";
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject("aaaaa")
                .claim("scope", scope)
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    private String getOwnKey(String keyName) throws IOException {
		String key = "";
		try (InputStream lpis = getClass().getResourceAsStream(keyName)) {
			if (lpis != null)
				key = new BufferedReader(new InputStreamReader(lpis, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
		} catch (IOException e1) {
			throw new FileNotFoundException(keyName);
		}
		if(StringUtils.isBlank(key)) {
			throw new IOException("Key "+keyName+" could not be read from class path");
		}
		return key;
	}
}
