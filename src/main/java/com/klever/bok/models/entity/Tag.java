package com.klever.bok.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties("tags")
    private Set<Card> cards;

    @PrePersist
    public void prePersist() {
        this.setId(UUID.randomUUID());
    }
}
