package com.github.eduoliveiradev.evolution.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SolicitacaoEmprestimoRequest(
        @Positive
        BigDecimal valor,
        @FutureOrPresent
        LocalDate dataPrimeiraParcela,
        @Min(1)
        @Max(60)
        Integer quantidadeParcela) {
}
