package com.klever.bok.services;

import com.klever.bok.exceptions.BadRequestException;
import com.klever.bok.exceptions.ResourceNotFoundException;
import com.klever.bok.models.CategoryDTO;
import com.klever.bok.models.entity.Category;
import com.klever.bok.payload.response.PagedResponse;
import com.klever.bok.repositories.CategoryRepository;
import com.klever.bok.security.model.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    public Category findById(UserPrincipal currentUser, UUID categoryId) {
        logger.info("CurrentUserID: {}, categoryId: {}", currentUser.getId(), categoryId);

        return categoryRepository.findByIdAndCreatedBy(categoryId, currentUser.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category", "uuid", categoryId)
                );
    }

    public Page<Category> findAllByUser(UserPrincipal userPrincipal, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "createdAt");
        Page<Category> categories = categoryRepository.findAllByCreatedBy(userPrincipal.getId(), pageable);

        logger.info("There are {} categories on requested page", categories.getTotalElements());

        return categories;
    }

    public Category createCategory(UserPrincipal currentUser, CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        logger.info("Category [before] setParentCategory : {}", category);
        category.setParentCategory(extractParentCategory(currentUser, categoryDTO));
        logger.info("Category [after] setParentCategory : {}", category);

        return categoryRepository.save(category);
    }

    public Category updateCategory(UserPrincipal currentUser, UUID categoryId, CategoryDTO categoryDTO) {
        Category categoryToUpdate = findById( currentUser,categoryId);

        logger.info("Category [before] updateFields : {}", categoryToUpdate);
        categoryToUpdate.setName(categoryDTO.getName());
        Category parentCategory = extractParentCategory(currentUser, categoryDTO);
        categoryToUpdate.setParentCategory(generateValidParentCategoryLink(categoryToUpdate, parentCategory));
        logger.info("Category [after] updateFields : {}", categoryToUpdate);
        return categoryRepository.save(categoryToUpdate);

    }

    private Category extractParentCategory(UserPrincipal currentUser, CategoryDTO categoryDTO) {
        logger.info("CategoryDTO: {}", categoryDTO);

        return categoryDTO.getParentCategoryId() != null ?
                findById(currentUser, categoryDTO.getParentCategoryId()) :
                null;
    }

    private Category generateValidParentCategoryLink(Category baseCategory, Category parentCategory) {
        if (parentCategory == null)
            return null;
        if (isValidChain(baseCategory, parentCategory))
            return parentCategory;
        else
            throw new BadRequestException("Invalid Parent-Children chain Generated, there is a loop in the proposed chain");
    }

    private boolean isValidChain(Category baseCategory, Category parentCategory) {
        Category currentCategory = parentCategory;
        while (currentCategory != null)
            if (currentCategory.getId().equals(baseCategory.getId()))
                return false;
            else
                currentCategory = currentCategory.getParentCategory();

        logger.info("Valid Chain for baseCategory: {}({}) and parentCategory: {}({})",
                baseCategory.getName(),
                baseCategory.getId(),
                parentCategory != null ? parentCategory.getName() : null,
                parentCategory != null ? parentCategory.getId() : null
        );
        return true;
    }
}
