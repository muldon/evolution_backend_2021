package com.github.eduoliveiradev.evolution.entity;


import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

public record Cliente(
        @Id
        UUID uuid,
        String nome,
        String email,
        String cpf,
        String rg,
        String endereco,
        BigDecimal renda,
        String senha
        ) {
}
