package com.github.eduoliveiradev.evolution.service;

import com.github.eduoliveiradev.evolution.dto.SolicitacaoEmprestimoDetalheResponse;
import com.github.eduoliveiradev.evolution.dto.SolicitacaoEmprestimoListagemResponse;
import com.github.eduoliveiradev.evolution.dto.SolicitacaoEmprestimoRequest;
import com.github.eduoliveiradev.evolution.entity.SolicitacaoEmprestimo;
import com.github.eduoliveiradev.evolution.repository.SolicitacaoEmprestimoRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SolicitacaoEmprestimoService {
    private final SolicitacaoEmprestimoRepository solicitacaoEmprestimoRepository;

    public SolicitacaoEmprestimoService(SolicitacaoEmprestimoRepository solicitacaoEmprestimoRepository) {
        this.solicitacaoEmprestimoRepository = solicitacaoEmprestimoRepository;
    }


    public UUID save(SolicitacaoEmprestimoRequest solicitacaoEmprestimoRequest) {
        SolicitacaoEmprestimo solicitacaoEmprestimo = transformer(solicitacaoEmprestimoRequest);
        final SolicitacaoEmprestimo save = solicitacaoEmprestimoRepository.save(solicitacaoEmprestimo);
        return save.uuid();

    }

    private SolicitacaoEmprestimo transformer(SolicitacaoEmprestimoRequest solicitacaoEmprestimoRequest) {
        UUID clienteUuid = getClienteUuid();
        return new SolicitacaoEmprestimo(null,
                clienteUuid,
                solicitacaoEmprestimoRequest.valor(),
                solicitacaoEmprestimoRequest.dataPrimeiraParcela(),
                solicitacaoEmprestimoRequest.quantidadeParcela()
                );
    }

    private UUID getClienteUuid() {
        String clienteUuid =  SecurityContextHolder.getContext().getAuthentication().getName();

        return  UUID.fromString(clienteUuid);
    }

    public List<SolicitacaoEmprestimoListagemResponse> findMySolicitacaoEmprestimo() {
        return solicitacaoEmprestimoRepository.findMySolicitacaoEmprestimo(getClienteUuid());
    }

    public Optional<SolicitacaoEmprestimoDetalheResponse> findDetalheEmprestimo(UUID uuidSolicitacaoEmprestimo) {
        return solicitacaoEmprestimoRepository.findDetalheEmprestimo(uuidSolicitacaoEmprestimo, getClienteUuid());
    }
}
