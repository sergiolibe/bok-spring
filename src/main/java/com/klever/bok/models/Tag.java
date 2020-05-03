package com.klever.bok.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString

@Entity
@Table(
        name = "tags")
public class Tag {
    @Id
    private UUID id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_id", nullable = false)
    private Tag parentCategory;

    @ManyToMany(mappedBy = "tags")
    private Set<Card> cards;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Date createdAt;
    private Date updatedAt;

    public Tag() {
    }

    @PrePersist
    public void prePersist() {
        this.setId(UUID.randomUUID());
        this.setCreatedAt(new Date());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(new Date());
    }
}
