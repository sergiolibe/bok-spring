package com.klever.bok.controllers;

import com.klever.bok.models.CategoryDTO;
import com.klever.bok.models.entity.Category;
import com.klever.bok.payload.response.PagedResponse;
import com.klever.bok.security.model.CurrentUser;
import com.klever.bok.security.model.UserPrincipal;
import com.klever.bok.services.CategoryService;
import com.klever.bok.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    //    @JsonView(Views.UserAccess.class)
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public PagedResponse<Category> getAll(@CurrentUser UserPrincipal currentUser,
                                          @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        logger.info("Fetching all categories for user {}", currentUser.getId());
        return categoryService.findAllByUser(currentUser, page, size);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{id}")
    public Category getById(@CurrentUser UserPrincipal currentUser,
                            @PathVariable UUID id) {
        return categoryService.findById(currentUser, id);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public Category create(@CurrentUser UserPrincipal currentUser,
                           @Valid @RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(currentUser, categoryDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public Category update(@CurrentUser UserPrincipal currentUser,
                           @PathVariable UUID id,
                           @Valid @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(currentUser, id, categoryDTO);
    }

}
