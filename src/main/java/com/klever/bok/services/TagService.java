package com.klever.bok.services;

import com.klever.bok.exceptions.ResourceNotFoundException;
import com.klever.bok.models.TagDTO;
import com.klever.bok.models.entity.Tag;
import com.klever.bok.repositories.TagRepository;
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

import java.util.*;

@Service
public class TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagService.class);

    @Autowired
    TagRepository tagRepository;

    @Autowired
    ModelMapper modelMapper;

    public Tag findById(UserPrincipal currentUser, UUID tagId) {
        return tagRepository.findByIdAndCreatedBy(tagId, currentUser.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Tag", "uuid", tagId)
                );
    }

    public Page<Tag> findAllByUser(UserPrincipal userPrincipal, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "createdAt");
        Page<Tag> tags = tagRepository.findAllByCreatedBy(userPrincipal.getId(), pageable);

        logger.info("There are {} tags on requested page", tags.getTotalElements());

        return tags;
    }

    public Set<Tag> findAllInIdSet(UserPrincipal userPrincipal, Set<UUID> tagIds) {
        Set<Tag> tags = tagRepository.findAllByIdInAndCreatedBy(tagIds, userPrincipal.getId());

//        tags.orElseThrow(() -> new ResourceNotFoundException("Tags", "Set<uuid>", tagIds));

        logger.info("There are {} tags on resulting set, {} where requested", tags.size(), tagIds.size());

        if (tags.size() < tagIds.size())
            throw new ResourceNotFoundException("Tags", "(at least one of) Set<uuid>", tagIds);

        return tags;
    }

    public Tag createTag(TagDTO tagDTO) {
        Tag tag = modelMapper.map(tagDTO, Tag.class);
        return tagRepository.save(tag);
    }

    public Tag updateTag(UserPrincipal currentUser, UUID tagId, TagDTO tagDTO) {
        return tagRepository.findByIdAndCreatedBy(tagId, currentUser.getId()).map(
                tag -> {
                    tag.setName(tagDTO.getName());
                    tag.setColor(tagDTO.getColor());
                    return tagRepository.save(tag);
                })
                .orElseThrow(
                        () -> new ResourceNotFoundException("Tag", "uuid", tagId)
                );
    }
}
