package com.klever.bok.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klever.bok.models.entity.audit.UserDateAudit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString

@Entity
@Table(
        name = "categories")
@JsonIgnoreProperties(
        value = {"cards", "id"},
        allowGetters = true
)
public class Category extends UserDateAudit {
    @Id
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_id", nullable = true)
    private Category parentCategory;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Card> cards;

    public Category() {
    }

    @PrePersist
    public void prePersist() {
        this.setId(UUID.randomUUID());
    }
}
