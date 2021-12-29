package com.github.eduoliveiradev.evolution.controller;

import com.github.eduoliveiradev.evolution.dto.SolicitacaoEmprestimoDetalheResponse;
import com.github.eduoliveiradev.evolution.dto.SolicitacaoEmprestimoListagemResponse;
import com.github.eduoliveiradev.evolution.dto.SolicitacaoEmprestimoRequest;
import com.github.eduoliveiradev.evolution.service.SolicitacaoEmprestimoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "JWT")
@RequestMapping("/solicitacao-emprestimo")
public class SolicitacaoEmprestimoController {
    private final SolicitacaoEmprestimoService solicitacaoEmprestimoService;

    public SolicitacaoEmprestimoController(SolicitacaoEmprestimoService solicitacaoEmprestimoService) {
        this.solicitacaoEmprestimoService = solicitacaoEmprestimoService;
    }

    @GetMapping
    public List<SolicitacaoEmprestimoListagemResponse> listar() {
        return solicitacaoEmprestimoService.findMySolicitacaoEmprestimo();
    }

    @GetMapping("/{uuidSolicitacaoEmprestimo}")
    public ResponseEntity<SolicitacaoEmprestimoDetalheResponse> detalhe(@PathVariable UUID uuidSolicitacaoEmprestimo) {
        return solicitacaoEmprestimoService.findDetalheEmprestimo(uuidSolicitacaoEmprestimo)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping
    public UUID criar(@RequestBody SolicitacaoEmprestimoRequest solicitacaoEmprestimoRequest) {
        return solicitacaoEmprestimoService.save(solicitacaoEmprestimoRequest);
    }
}
