package com.example.inventory.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory.infrastructure.persistence.entity.OutboxEntity;

/**
 * Repositorio JPA para la entidad Outbox.
 */
public interface OutboxRepository extends JpaRepository<OutboxEntity, String> {

}
