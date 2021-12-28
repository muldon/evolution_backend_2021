package com.github.eduoliveiradev.evolution.repository;

import com.github.eduoliveiradev.evolution.dto.SolicitacaoEmprestimoListagemResponse;
import com.github.eduoliveiradev.evolution.entity.SolicitacaoEmprestimo;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface SolicitacaoEmprestimoRepository extends CrudRepository<SolicitacaoEmprestimo, UUID> {

    @Query("SELECT uuid as codigo_emprestimo, valor, quantidade_parcela FROM solicitacao_emprestimo WHERE cliente_uuid = :clienteUuid")
    List<SolicitacaoEmprestimoListagemResponse> findMySolicitacaoEmprestimo(UUID clienteUuid);
}
