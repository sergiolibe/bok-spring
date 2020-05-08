package com.klever.bok.controllers;

import com.klever.bok.models.entity.Card;
import com.klever.bok.payload.response.PagedResponse;
import com.klever.bok.security.model.CurrentUser;
import com.klever.bok.security.model.UserPrincipal;
import com.klever.bok.services.CardService;
import com.klever.bok.services.UserService;
import com.klever.bok.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public PagedResponse<Card> getAll(@CurrentUser UserPrincipal currentUser,
                                      @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return cardService.getAllCards(currentUser, page, size);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @PostMapping()
    public Card create(@CurrentUser UserPrincipal currentUser,
                       @RequestBody final Card card) {
//        User user = userService.userFromUserDetails(userDetails);
//        logger.info("before setting userId, Card: {}", card);
//        card.setUser(user);
//        logger.info("after  setting userId, Card: {}", card);
        return cardService.createCard(card);
    }

}
