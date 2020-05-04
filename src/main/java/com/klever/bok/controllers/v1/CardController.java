package com.klever.bok.controllers.v1;

import com.klever.bok.controllers.ApiVersion;
import com.klever.bok.models.Card;
import com.klever.bok.models.User;
import com.klever.bok.repositories.CardRepository;
import com.klever.bok.security.services.UserDetailsImpl;
import com.klever.bok.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiVersion.V1 + "/cards")
public class CardController {

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<Card> getAll(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return cardRepository.findByUserId(userDetails.getId());
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @PostMapping()
    public Card create(@AuthenticationPrincipal UserDetails userDetails,
                       @RequestBody final Card card) {
        User user = userService.userFromUserDetails(userDetails);
        logger.info("before setting userId, Card: {}", card);
        card.setUser(user);
        logger.info("after  setting userId, Card: {}", card);
        return cardRepository.saveAndFlush(card);
    }

}
