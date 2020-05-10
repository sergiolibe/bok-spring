package com.klever.bok.models.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klever.bok.models.Views;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryTest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryTest.class);

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializingWithJsonView() throws JsonProcessingException {

        UUID categoryId = UUID.randomUUID();
        String categoryName = "Zero Category";
        UUID userId = UUID.randomUUID();

        Category testCategory = Category.builder()
                .id(categoryId)
                .name(categoryName)
                .createdBy(userId)
                .build();

        String jsonStringQuick = objectMapper.writerWithView(Views.Quick.class)
                .writeValueAsString(testCategory);

        String jsonStringComplete = objectMapper.writerWithView(Views.Complete.class)
                .writeValueAsString(testCategory);

        logger.debug("testCategory: {}", testCategory);
        logger.debug("testCategory (Quick):\t\t {}", jsonStringQuick);
        logger.debug("testCategory (Complete):\t {}", jsonStringComplete);

        assertTrue(jsonStringQuick.contains(categoryName));

    }

}