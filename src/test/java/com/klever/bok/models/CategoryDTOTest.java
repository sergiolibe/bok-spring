package com.klever.bok.models;

import com.klever.bok.models.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDTOTest {

    ModelMapper modelMapper;

    @BeforeEach
    void setUp(){
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }


    @Test
    void whenMapCategoryDTO_thenReturnCategory() {
        String categoryName = "Programming";
        UUID parentCategoryId = UUID.randomUUID();
        CategoryDTO categoryDTO;
        Category category;

        categoryDTO = new CategoryDTO();
        categoryDTO.setName(categoryName);
        category = modelMapper.map(categoryDTO, Category.class);

        assertEquals(category.getName(), categoryDTO.getName());
        assertNull(category.getId());
        assertNull(category.getParentCategory());


        categoryDTO = new CategoryDTO();
        categoryDTO.setName(categoryName);
        categoryDTO.setParentCategoryId(parentCategoryId);
        category = modelMapper.map(categoryDTO, Category.class);

        assertEquals(category.getName(), categoryDTO.getName());
        assertNull(category.getId());
        assertNull(category.getParentCategory());


    }

    @Test
    void whenMapCategoryDTO_andModelMapperWithDefaultMatchingStrategy_thenReturnBadCategory() {
        String categoryName = "Programming";
        UUID parentCategoryId = UUID.randomUUID();
        CategoryDTO categoryDTO;
        Category category;

        modelMapper = new ModelMapper();
        categoryDTO = new CategoryDTO();
        categoryDTO.setName(categoryName);
        categoryDTO.setParentCategoryId(parentCategoryId);
        category = modelMapper.map(categoryDTO, Category.class);

        assertEquals(category.getName(), categoryDTO.getName());
        assertNotNull(category.getId());
        assertNotNull(category.getParentCategory());
    }
}