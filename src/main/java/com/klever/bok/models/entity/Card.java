package com.klever.bok.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.klever.bok.models.Views;
import com.klever.bok.models.entity.audit.UserDateAudit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString

@Entity
@Table(
        name = "cards")
@JsonView({Views.Quick.class,Views.Complete.class})
public class Card extends UserDateAudit {
    @Id
    private UUID id;

    private String title;

    @Type(type = "text")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties("cards")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "cards_tags",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnoreProperties("cards")
    private Set<Tag> tags;

    public Card() {
    }

    @PrePersist
    public void prePersist() {
        this.setId(UUID.randomUUID());
    }
}
