package com.github.eduoliveiradev.evolution.repository;

import com.github.eduoliveiradev.evolution.dto.SolicitacaoEmprestimoDetalheResponse;
import com.github.eduoliveiradev.evolution.dto.SolicitacaoEmprestimoListagemResponse;
import com.github.eduoliveiradev.evolution.entity.SolicitacaoEmprestimo;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SolicitacaoEmprestimoRepository extends CrudRepository<SolicitacaoEmprestimo, UUID> {

    @Query("SELECT uuid as codigo_emprestimo, valor, quantidade_parcela FROM solicitacao_emprestimo WHERE cliente_uuid = :clienteUuid")
    List<SolicitacaoEmprestimoListagemResponse> findMySolicitacaoEmprestimo(UUID clienteUuid);

    @Query(""" 
              SELECT s.uuid as codigo_emprestimo, s.valor, s.quantidade_parcela, s.data_primeira_parcela, c.email, c.renda
              FROM solicitacao_emprestimo s
              INNER JOIN cliente c ON c.uuid = s.cliente_uuid
              WHERE s.uuid = :uuidSolicitacaoEmprestimo
              AND s.cliente_uuid = :clienteUuid
            """)
    Optional<SolicitacaoEmprestimoDetalheResponse> findDetalheEmprestimo(UUID uuidSolicitacaoEmprestimo, UUID clienteUuid);
}
