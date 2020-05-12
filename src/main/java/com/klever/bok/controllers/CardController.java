package com.klever.bok.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.klever.bok.models.CardDTO;
import com.klever.bok.models.Views;
import com.klever.bok.models.entity.Card;
import com.klever.bok.security.model.CurrentUser;
import com.klever.bok.security.model.UserPrincipal;
import com.klever.bok.services.CardService;
import com.klever.bok.services.UserService;
import com.klever.bok.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    CardService cardService;

    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @JsonView(Views.Quick.class)
    public Page<Card> getAll(@CurrentUser UserPrincipal currentUser,
                             @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                             @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return cardService.findAllByUser(currentUser, page, size);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("{id}")
    @JsonView(Views.Complete.class)
    public Card getById(@CurrentUser UserPrincipal currentUser,
                        @PathVariable UUID id) {
        return cardService.findById(currentUser, id);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    @JsonView(Views.Complete.class)
    public Card create(@CurrentUser UserPrincipal currentUser,
                       @Valid @RequestBody final CardDTO cardDTO) {
        return cardService.createCard(currentUser, cardDTO);
    }

}
