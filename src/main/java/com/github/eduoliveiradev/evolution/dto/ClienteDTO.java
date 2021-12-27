package com.github.eduoliveiradev.evolution.dto;

import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ClienteDTO(
        @NotBlank
        String nome,
        @Email
        @NotBlank
        String email,
        @CPF
        String cpf,
        @NotBlank
        String rg,
        @NotBlank
        String endereco,
        @NotNull
        BigDecimal renda,
        @NotBlank
        String senha) {
}
