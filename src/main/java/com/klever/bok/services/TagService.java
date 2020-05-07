package com.klever.bok.services;

import com.klever.bok.exceptions.ResourceNotFoundException;
import com.klever.bok.models.Tag;
import com.klever.bok.payload.response.PagedResponse;
import com.klever.bok.repositories.TagRepository;
import com.klever.bok.security.model.UserPrincipal;
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
public class TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagService.class);

    @Autowired
    TagRepository tagRepository;

    public PagedResponse<Tag> findAllByUser(UserPrincipal userPrincipal, int page, int pageSize) {
        PagedResponse.validatePageNumberAndSize(page, pageSize);

        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "createdAt");
        Page<Tag> tags = tagRepository.findAllByCreatedBy(userPrincipal.getId(), pageable);

        logger.info("There are {} tags on requested page", tags.getTotalElements());

        return new PagedResponse<>(
                tags.getNumberOfElements() > 0 ? tags.getContent() : Collections.emptyList(),
                tags.getNumber(),
                tags.getSize(),
                tags.getTotalElements(),
                tags.getTotalPages(),
                tags.isLast()
        );

    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag updateTag(UserPrincipal currentUser, UUID tagId, Tag modifiedTag) {
        return tagRepository.findByIdAndCreatedBy(tagId, currentUser.getId()).map(
                tag -> {
                    tag.setName(modifiedTag.getName());
                    tag.setColor(modifiedTag.getColor());
                    return tagRepository.save(tag);
                })
                .orElseThrow(
                        () -> new ResourceNotFoundException("Card", "uuid", tagId)
                );
    }

    public Tag findById(UserPrincipal currentUser, UUID tagId) {
        return tagRepository.findByIdAndCreatedBy(tagId, currentUser.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Card", "uuid", tagId)
                );
    }
}
