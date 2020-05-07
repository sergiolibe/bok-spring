package com.klever.bok.repositories;

import com.klever.bok.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    Page<Tag> findAllByCreatedBy(UUID userId, Pageable pageable);
    Optional<Tag> findByIdAndCreatedBy(UUID tagId,UUID userId);
}
