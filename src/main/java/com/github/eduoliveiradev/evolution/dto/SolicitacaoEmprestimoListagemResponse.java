package com.github.eduoliveiradev.evolution.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SolicitacaoEmprestimoListagemResponse(
        UUID codigoEmprestimo,
        BigDecimal valor,
        Integer quantidadeParcela) {
}
