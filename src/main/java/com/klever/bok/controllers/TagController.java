package com.klever.bok.controllers;

import com.klever.bok.models.TagDTO;
import com.klever.bok.models.entity.Tag;
import com.klever.bok.payload.response.PagedResponse;
import com.klever.bok.security.model.CurrentUser;
import com.klever.bok.security.model.UserPrincipal;
import com.klever.bok.services.TagService;
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
@RequestMapping("/api/tags")
public class TagController {

    private static final Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    TagService tagService;

    //    @JsonView(Views.UserAccess.class)
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public PagedResponse<Tag> getAll(@CurrentUser UserPrincipal currentUser,
                                     @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                     @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        logger.info("Fetching all tags for user {}", currentUser.getId());
        return tagService.findAllByUser(currentUser, page, size);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{id}")
    public Tag getById(@CurrentUser UserPrincipal currentUser,
                       @PathVariable UUID id) {
        return tagService.findById(currentUser, id);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public Tag create(@Valid @RequestBody TagDTO tagDTO) {
        return tagService.createTag(tagDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{id}")
    public Tag update(@CurrentUser UserPrincipal currentUser,
                      @PathVariable UUID id,
                      @Valid @RequestBody TagDTO tagDTO) {
        return tagService.updateTag(currentUser, id, tagDTO);
    }

}
