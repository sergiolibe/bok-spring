package com.klever.bok.services;

import com.klever.bok.exceptions.ResourceNotFoundException;
import com.klever.bok.models.CardDTO;
import com.klever.bok.models.entity.Card;
import com.klever.bok.models.entity.Tag;
import com.klever.bok.repositories.CardRepository;
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

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CardService {

    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    TagService tagService;

    @Autowired
    ModelMapper modelMapper;

    public Card findById(UserPrincipal currentUser, UUID cardId) {
        logger.info("CurrentUserID: {}, cardId: {}", currentUser.getId(), cardId);

        return cardRepository.findByIdAndCreatedBy(cardId, currentUser.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Card", "uuid", cardId)
                );
    }

    public Page<Card> findAllByUser(UserPrincipal userPrincipal, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "createdAt");
        Page<Card> cards = cardRepository.findAllByCreatedBy(userPrincipal.getId(), pageable);

        logger.info("There are {} categories on requested page", cards.getTotalElements());

        return cards;
    }

    public Card createCard(UserPrincipal currentUser, CardDTO cardDTO) {
        Card card = modelMapper.map(cardDTO, Card.class);
        card.setCategory(categoryService.findById(currentUser, cardDTO.getCategoryId()));
        card.setTags(obtainTags(currentUser, cardDTO));
        return cardRepository.save(card);
    }

    private Set<Tag> obtainTags(UserPrincipal currentUser, CardDTO cardDTO) {
        return cardDTO.getTagIds() != null ?
                tagService.findAllInIdSet(currentUser, cardDTO.getTagIds()) :
                null;
    }
}
