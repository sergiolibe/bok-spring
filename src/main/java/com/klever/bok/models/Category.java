package com.klever.bok.models;

import com.klever.bok.models.audit.UserDateAudit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString

@Entity
@Table(
        name = "categories")
public class Category extends UserDateAudit {
    @Id
    private UUID id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_id", nullable = false)
    private Category parentCategory;

    public Category() {
    }

    @PrePersist
    public void prePersist() {
        this.setId(UUID.randomUUID());
    }
}
