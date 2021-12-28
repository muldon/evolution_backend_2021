package com.github.eduoliveiradev.evolution.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SolicitacaoEmprestimoRequest(
        BigDecimal valor,
        LocalDate dataPrimeiraParcela,
        Integer quantidadeParcela) {
}
