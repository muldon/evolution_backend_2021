package com.github.eduoliveiradev.evolution.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SolicitacaoEmprestimoDetalheResponse(
        UUID codigoEmprestimo,
        BigDecimal valor,
        Integer quantidadeParcela,
        LocalDate dataPrimeiraParcela,
        String email,
        BigDecimal renda) {
}
