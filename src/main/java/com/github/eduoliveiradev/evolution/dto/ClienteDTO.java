package com.github.eduoliveiradev.evolution.dto;

import java.math.BigDecimal;

public record ClienteDTO(
        String nome,
        String email,
        String cpf,
        String rg,
        String endereco,
        BigDecimal renda,
        String senha) {
}
