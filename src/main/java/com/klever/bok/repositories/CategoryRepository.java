package com.klever.bok.repositories;

import com.klever.bok.models.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Page<Category> findAllByCreatedBy(UUID userId, Pageable pageable);

    Optional<Category> findByIdAndCreatedBy(UUID categoryId, UUID userId);
}
