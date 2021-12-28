package com.github.eduoliveiradev.evolution.entity;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SolicitacaoEmprestimo(
        @Id
        UUID uuid,
        UUID clienteUuid,
        BigDecimal valor,
        LocalDate dataPrimeiraParcela,
        Integer quantidadeParcela) {
}
