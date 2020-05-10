package com.klever.bok.repositories;

import com.klever.bok.models.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    Page<Card> findAllByCreatedBy(UUID userId, Pageable pageable);

    Optional<Card> findByIdAndCreatedBy(UUID cardId, UUID userId);
}
