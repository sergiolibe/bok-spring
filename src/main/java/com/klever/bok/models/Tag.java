package com.klever.bok.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.klever.bok.models.audit.UserDateAudit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString

@Entity
@Table(
        name = "tags"
//        ,
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = {"name", "created_by"})
//        }
)
@JsonIgnoreProperties(
        value = {"cards", "id"},
        allowGetters = true
)
public class Tag extends UserDateAudit {
    @Id
    private UUID id;

    private String name;

    private String color;

    @ManyToMany(mappedBy = "tags")
    private Set<Card> cards;

    public Tag() {
    }

    @PrePersist
    public void prePersist() {
        this.setId(UUID.randomUUID());
    }
}
