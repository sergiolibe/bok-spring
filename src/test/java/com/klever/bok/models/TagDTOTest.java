package com.klever.bok.models;

import com.klever.bok.models.entity.Category;
import com.klever.bok.models.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TagDTOTest {

    ModelMapper modelMapper;

    @BeforeEach
    void setUp(){
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Test
    void whenMapTagDTO_thenReturnTag() {
        String tagName = "Spring";
        String tagColor = "#f123ab";
        TagDTO tagDTO;
        Tag tag;

        tagDTO = new TagDTO();
        tagDTO.setName(tagName);
        tagDTO.setColor(tagColor);
        tag = modelMapper.map(tagDTO, Tag.class);

        assertEquals(tag.getName(), tagDTO.getName());
        assertEquals(tag.getColor(), tagDTO.getColor());
        assertNull(tag.getId());
    }
}