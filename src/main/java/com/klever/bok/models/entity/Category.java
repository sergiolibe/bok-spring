package com.klever.bok.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.klever.bok.models.Views;
import com.klever.bok.models.entity.audit.UserDateAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder

@Entity
@Table(
        name = "categories")
@JsonIgnoreProperties(
        value = {"cards", "id"},
        allowGetters = true
)
@JsonView(Views.Complete.class)
public class Category extends UserDateAudit {
    @JsonView(Views.Quick.class)
    @Id
    private UUID id;

    @JsonView(Views.Quick.class)
    private String name;

    @JsonView(Views.Quick.class)
//    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "parent_id", nullable = true)
    private Category parentCategory;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonIgnoreProperties("category")
    private Set<Card> cards;

    @PrePersist
    public void prePersist() {
        this.setId(UUID.randomUUID());
    }
}
