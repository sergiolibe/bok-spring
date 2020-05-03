package com.klever.bok.repositories;

import com.klever.bok.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUserId(UUID userId);
}
