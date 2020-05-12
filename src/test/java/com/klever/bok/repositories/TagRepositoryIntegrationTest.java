package com.klever.bok.repositories;

import com.klever.bok.models.entity.Tag;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class TagRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TagRepository tagRepository;

    private UUID userId1;
    private Tag tag1user1;
    private Tag tag2user1;
    private UUID userId2;
    private Tag tag1user2;
    private UUID userId3;


    @BeforeEach
    void setUp() {
        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        userId3 = UUID.randomUUID();

        tag1user1 = Tag.builder()
                .name("Programming")
                .color("#123456")
                .createdBy(userId1).build();
        entityManager.persist(tag1user1);

        tag2user1 = Tag.builder()
                .name("Agile")
                .color("#000fff")
                .createdBy(userId1).build();
        entityManager.persist(tag2user1);

        tag1user2 = Tag.builder()
                .name("Culture")
                .color("#f1e2d3")
                .createdBy(userId2).build();
        entityManager.persist(tag1user2);

        entityManager.flush();
    }

    @Test
    void whenFindAllByCreatedBy_thenReturnTags() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

        Page<Tag> page;

        page = tagRepository.findAllByCreatedBy(userId3, pageable);
        assertEquals(page.getNumberOfElements(), 0);

        page = tagRepository.findAllByCreatedBy(userId2, pageable);
        assertEquals(page.getNumberOfElements(), 1);

        page = tagRepository.findAllByCreatedBy(userId1, pageable);
        assertEquals(page.getNumberOfElements(), 2);
    }

    @Test
    void findByIdAndCreatedBy() {

        Optional<Tag> tag;

        tag = tagRepository.findByIdAndCreatedBy(UUID.randomUUID(), userId1);
        assertFalse(tag.isPresent());

        tag = tagRepository.findByIdAndCreatedBy(tag1user1.getId(), userId3);
        assertFalse(tag.isPresent());

        tag = tagRepository.findByIdAndCreatedBy(tag1user1.getId(), userId1);
        assertTrue(tag.isPresent());
        tag = tagRepository.findByIdAndCreatedBy(tag2user1.getId(), userId1);
        assertTrue(tag.isPresent());

        tag = tagRepository.findByIdAndCreatedBy(tag1user2.getId(), userId2);
        assertTrue(tag.isPresent());
    }

    @Test
    void findAllByIdInAndCreatedBy() {

        Set<Tag> tags;
        Set<UUID> tagIds = new HashSet<>();

        tagIds.add(UUID.randomUUID());
        tags = tagRepository.findAllByIdInAndCreatedBy(tagIds, userId1);
        assertEquals(0,tags.size());

        tagIds.clear();
        tagIds.add(tag1user1.getId());
        tags = tagRepository.findAllByIdInAndCreatedBy(tagIds, userId3);
        assertEquals(0,tags.size());

        tagIds.clear();
        tagIds.add(tag1user1.getId());
        tagIds.add(tag1user2.getId());
        tags = tagRepository.findAllByIdInAndCreatedBy(tagIds, userId1);
        assertEquals(1,tags.size());

        tagIds.clear();
        tagIds.add(tag1user1.getId());
        tagIds.add(UUID.randomUUID());
        tags = tagRepository.findAllByIdInAndCreatedBy(tagIds, userId1);
        assertEquals(1,tags.size());

        tagIds.clear();
        tagIds.add(tag1user1.getId());
        tagIds.add(tag2user1.getId());
        tags = tagRepository.findAllByIdInAndCreatedBy(tagIds, userId1);
        assertEquals(2,tags.size());

        tagIds.clear();
        tagIds.add(tag1user2.getId());
        tags = tagRepository.findAllByIdInAndCreatedBy(tagIds, userId2);
        assertEquals(1,tags.size());

    }
}