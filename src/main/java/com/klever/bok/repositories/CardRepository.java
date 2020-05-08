package com.klever.bok.repositories;

import com.klever.bok.models.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    Optional<List<Card>> findByCreatedBy(UUID userId);
}
