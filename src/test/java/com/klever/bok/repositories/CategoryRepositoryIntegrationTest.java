package com.klever.bok.repositories;

import com.klever.bok.models.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.data.jpa.domain.AbstractAuditable_.createdBy;

@RunWith(SpringRunner.class)
@DataJpaTest
class CategoryRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private UUID userId1;
    private Category category1user1;
    private Category category2user1;
    private UUID userId2;
    private Category category1user2;
    private UUID userId3;


    @BeforeEach
    void setUp() {
        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        userId3 = UUID.randomUUID();

        category1user1 = Category.builder()
                .name("Programming")
                .createdBy(userId1).build();
        entityManager.persist(category1user1);

        category2user1 = Category.builder()
                .name("Agile")
                .createdBy(userId1).build();
        entityManager.persist(category2user1);

        category1user2 = Category.builder()
                .name("Culture")
                .createdBy(userId2).build();
        entityManager.persist(category1user2);

        entityManager.flush();
    }

    @Test
    void whenFindAllByCreatedBy_thenReturnCategories() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

        Page<Category> page;

        page = categoryRepository.findAllByCreatedBy(userId3, pageable);
        assertEquals(page.getNumberOfElements(), 0);

        page = categoryRepository.findAllByCreatedBy(userId2, pageable);
        assertEquals(page.getNumberOfElements(), 1);

        page = categoryRepository.findAllByCreatedBy(userId1, pageable);
        assertEquals(page.getNumberOfElements(), 2);
    }

    @Test
    void findByIdAndCreatedBy() {

        Optional<Category> category;

        category = categoryRepository.findByIdAndCreatedBy(UUID.randomUUID(), userId1);
        assertFalse(category.isPresent());

        category = categoryRepository.findByIdAndCreatedBy(category1user1.getId(), userId3);
        assertFalse(category.isPresent());

        category = categoryRepository.findByIdAndCreatedBy(category1user1.getId(), userId1);
        assertTrue(category.isPresent());
        category = categoryRepository.findByIdAndCreatedBy(category2user1.getId(), userId1);
        assertTrue(category.isPresent());

        category = categoryRepository.findByIdAndCreatedBy(category1user2.getId(), userId2);
        assertTrue(category.isPresent());
    }
}