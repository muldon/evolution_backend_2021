package com.github.eduoliveiradev.evolution.repository;

import com.github.eduoliveiradev.evolution.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends CrudRepository<Cliente, UUID> {

    Optional<Cliente> findByEmail(String email);
}
