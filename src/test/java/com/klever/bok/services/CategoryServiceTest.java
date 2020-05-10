package com.klever.bok.services;

import com.klever.bok.exceptions.BadRequestException;
import com.klever.bok.exceptions.ResourceNotFoundException;
import com.klever.bok.models.CategoryDTO;
import com.klever.bok.models.entity.Category;
import com.klever.bok.payload.response.PagedResponse;
import com.klever.bok.repositories.CategoryRepository;
import com.klever.bok.security.model.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.data.jpa.domain.AbstractAuditable_.createdBy;

@RunWith(SpringRunner.class)
@SpringBootTest
class CategoryServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceTest.class);

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private CategoryService categoryService;


    private UserPrincipal user1;

    private String zeroCategoryName;
    private UUID zeroCategoryId;
    private Category zeroCategoryPersisted;

    private String firstCategoryName;
    private UUID firstCategoryId;
    private Category firstCategoryPersisted;

    private String secondCategoryName;
    private UUID secondCategoryId;
    private Category secondCategoryPersisted;

    private String thirdCategoryName;
    private UUID thirdCategoryId;
    private Category thirdCategoryPersisted;

    @BeforeEach
    void setUp() {
        user1 = new UserPrincipal();
        user1.setId(UUID.randomUUID());

        zeroCategoryName = "Zero Category";
        zeroCategoryId = UUID.randomUUID();
        zeroCategoryPersisted = Category.builder()
                .id(zeroCategoryId)
                .name(zeroCategoryName)
                .createdBy(user1.getId())
                .build();

        firstCategoryName = "First Category";
        firstCategoryId = UUID.randomUUID();
        firstCategoryPersisted = Category.builder()
                .id(firstCategoryId)
                .name(firstCategoryName)
                .createdBy(user1.getId())
                .build();

        secondCategoryName = "Second Category";
        secondCategoryId = UUID.randomUUID();
        secondCategoryPersisted = Category.builder()
                .id(secondCategoryId)
                .name(secondCategoryName)
                .createdBy(user1.getId())
                .parentCategory(firstCategoryPersisted)
                .build();

        thirdCategoryName = "Third Category";
        thirdCategoryId = UUID.randomUUID();
        thirdCategoryPersisted = Category.builder()
                .id(thirdCategoryId)
                .name(thirdCategoryName)
                .createdBy(user1.getId())
                .parentCategory(secondCategoryPersisted)
                .build();
    }

    @Test
    void shouldCreateCategoryWithoutParent() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(firstCategoryName);

        Category firstCategoryAfterMap = new Category();
        firstCategoryAfterMap.setName(firstCategoryName);

        when(modelMapper.map(categoryDTO, Category.class)).thenReturn(firstCategoryAfterMap);

        when(categoryRepository.findByIdAndCreatedBy(firstCategoryId, user1.getId()))
                .thenReturn(Optional.of(firstCategoryPersisted));

        when(categoryRepository.save(firstCategoryAfterMap)).thenReturn(firstCategoryPersisted);


        Category savedCategory = categoryService.createCategory(user1, categoryDTO);

        assertNotNull(savedCategory);
        assertNull(savedCategory.getParentCategory());
        assertEquals(savedCategory.getId(), firstCategoryId);
        assertEquals(savedCategory.getName(), firstCategoryName);
        assertEquals(savedCategory.getCreatedBy(), user1.getId());
    }

    @Test
    void shouldCreateCategoryWithParent() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(secondCategoryName);
        categoryDTO.setParentCategoryId(firstCategoryId);

        Category secondCategoryAfterMap = new Category();
        secondCategoryAfterMap.setName(secondCategoryName);

        when(modelMapper.map(categoryDTO, Category.class)).thenReturn(secondCategoryAfterMap);

        when(categoryRepository.findByIdAndCreatedBy(firstCategoryId, user1.getId()))
                .thenReturn(Optional.of(firstCategoryPersisted));

        when(categoryRepository.save(secondCategoryAfterMap)).thenReturn(secondCategoryPersisted);


        Category savedCategory = categoryService.createCategory(user1, categoryDTO);

        assertNotNull(savedCategory);
        assertEquals(savedCategory.getId(), secondCategoryId);
        assertEquals(savedCategory.getName(), secondCategoryName);
        assertEquals(savedCategory.getCreatedBy(), user1.getId());

        assertNotNull(savedCategory.getParentCategory());
        assertEquals(savedCategory.getParentCategory().getId(), firstCategoryId);
        assertEquals(savedCategory.getParentCategory().getName(), firstCategoryName);
        assertEquals(savedCategory.getParentCategory().getCreatedBy(), user1.getId());
    }

    @Test
    void shouldNotCreateCategoryWithParentWrongId() {
        final UUID wrongParentCategoryId = UUID.randomUUID();

        CategoryDTO modifiedFirstCategoryDTO = new CategoryDTO();
        modifiedFirstCategoryDTO.setName(firstCategoryName);
        modifiedFirstCategoryDTO.setParentCategoryId(wrongParentCategoryId);

        Category firstCategoryAfterMap = new Category();
        firstCategoryAfterMap.setName(firstCategoryName);

        when(modelMapper.map(modifiedFirstCategoryDTO, Category.class)).thenReturn(firstCategoryAfterMap);

        when(categoryRepository.findByIdAndCreatedBy(zeroCategoryId, user1.getId()))
                .thenReturn(Optional.of(zeroCategoryPersisted));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.createCategory(user1, modifiedFirstCategoryDTO);
        });

        String expectedMessage = (new ResourceNotFoundException(Category.class.getSimpleName(), "uuid", wrongParentCategoryId)).getMessage();
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldUpdateCategoryWithGoodParentChain() {

        CategoryDTO modifiedFirstCategoryDTO = new CategoryDTO();
        modifiedFirstCategoryDTO.setName(firstCategoryName);
        modifiedFirstCategoryDTO.setParentCategoryId(zeroCategoryId);

        Category firstCategoryAfterMap = Category.builder()
                .name(firstCategoryName)
                .build();

        Category firstCategoryModified = Category.builder()
                .id(firstCategoryId)
                .name(firstCategoryName)
                .createdBy(user1.getId())
                .parentCategory(zeroCategoryPersisted)
                .build();

        when(modelMapper.map(modifiedFirstCategoryDTO, Category.class)).thenReturn(firstCategoryAfterMap);

        when(categoryRepository.findByIdAndCreatedBy(zeroCategoryId, user1.getId()))
                .thenReturn(Optional.of(zeroCategoryPersisted));

        when(categoryRepository.findByIdAndCreatedBy(firstCategoryId, user1.getId()))
                .thenReturn(Optional.of(firstCategoryPersisted));

        when(categoryRepository.findByIdAndCreatedBy(secondCategoryId, user1.getId()))
                .thenReturn(Optional.of(secondCategoryPersisted));

        when(categoryRepository.findByIdAndCreatedBy(thirdCategoryId, user1.getId()))
                .thenReturn(Optional.of(thirdCategoryPersisted));

        when(categoryRepository.save(Mockito.any(Category.class))).thenAnswer(i -> i.getArguments()[0]);

        Category updatedCategory = categoryService.updateCategory(user1, firstCategoryId, modifiedFirstCategoryDTO);

        assertNotNull(updatedCategory);
        assertEquals(firstCategoryId, updatedCategory.getId());
        assertEquals(firstCategoryName, updatedCategory.getName());
        assertEquals(user1.getId(),updatedCategory.getCreatedBy());
        assertNotNull(updatedCategory.getParentCategory());
        assertNotNull(updatedCategory.getParentCategory().getId());
        assertEquals(zeroCategoryId, updatedCategory.getParentCategory().getId());
    }

    @Test
    void shouldNotUpdateCategoryWithBadParentChain() {
        CategoryDTO modifiedFirstCategoryDTO = new CategoryDTO();
        modifiedFirstCategoryDTO.setName(firstCategoryName);
        modifiedFirstCategoryDTO.setParentCategoryId(thirdCategoryId);

        Category firstCategoryAfterMap = Category.builder()
                .name(firstCategoryName)
                .build();

        when(modelMapper.map(modifiedFirstCategoryDTO, Category.class)).thenReturn(firstCategoryAfterMap);

//        when(categoryRepository.findByIdAndCreatedBy(zeroCategoryId, user1.getId()))
//                .thenReturn(Optional.of(zeroCategoryPersisted));

        when(categoryRepository.findByIdAndCreatedBy(firstCategoryId, user1.getId()))
                .thenReturn(Optional.of(firstCategoryPersisted));

//        when(categoryRepository.findByIdAndCreatedBy(secondCategoryId, user1.getId()))
//                .thenReturn(Optional.of(secondCategoryPersisted));

        when(categoryRepository.findByIdAndCreatedBy(thirdCategoryId, user1.getId()))
                .thenReturn(Optional.of(thirdCategoryPersisted));

        assertThrows(BadRequestException.class, () -> {
            categoryService.updateCategory(user1, firstCategoryId, modifiedFirstCategoryDTO);
        });
    }

    @Test
    void shouldUpdateCategoryWithParentNull() {
        CategoryDTO modifiedSecondCategoryDTO = new CategoryDTO();
        modifiedSecondCategoryDTO.setName(secondCategoryName);
        modifiedSecondCategoryDTO.setParentCategoryId(null);

        Category secondCategoryAfterMap = Category.builder()
                .name(secondCategoryName)
                .build();

        when(modelMapper.map(modifiedSecondCategoryDTO, Category.class)).thenReturn(secondCategoryAfterMap);

        when(categoryRepository.findByIdAndCreatedBy(secondCategoryId, user1.getId()))
                .thenReturn(Optional.of(secondCategoryPersisted));

        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArguments()[0]);

        Category secondCategoryUpdated = categoryService.updateCategory(user1, secondCategoryId, modifiedSecondCategoryDTO);

        assertNotNull(secondCategoryUpdated);
        assertEquals(secondCategoryId, secondCategoryUpdated.getId());
        assertEquals(secondCategoryName, secondCategoryUpdated.getName());
        assertEquals(user1.getId(),secondCategoryUpdated.getCreatedBy());
        assertNull(secondCategoryUpdated.getParentCategory());
    }

//    @Test
//    void shouldReturnAllCategoriesCreatedByUser() {
//        PagedResponse<Category> pagedResponse = new PagedResponse<>();
//    }
}