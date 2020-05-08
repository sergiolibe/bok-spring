package com.klever.bok.services;

import com.klever.bok.models.entity.Card;
import com.klever.bok.payload.response.PagedResponse;
import com.klever.bok.repositories.CardRepository;
import com.klever.bok.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CardService {

    @Autowired
    CardRepository cardRepository;

    public PagedResponse<Card> getAllCards(UserPrincipal userPrincipal, int page, int pageSize) {
        PagedResponse.validatePageNumberAndSize(page, pageSize);


        // Retrieve Cards
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "createdAt");
        Page<Card> cards = cardRepository.findAll(pageable);

        return new PagedResponse<>(
                cards.getNumberOfElements() > 0 ? cards.getContent() : Collections.emptyList(),
                cards.getNumber(),
                cards.getSize(),
                cards.getTotalElements(),
                cards.getTotalPages(),
                cards.isLast()
        );

    }

    public Card createCard(Card card) {
        return cardRepository.save(card);
    }
}
