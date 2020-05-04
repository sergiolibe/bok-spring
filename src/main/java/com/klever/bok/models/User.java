package com.klever.bok.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
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
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class User {

    @Id
    private UUID id;

    @NotBlank
    @Size(min = 6, max = 20)
    private String name;

    @NotBlank
    @Size(min = 6, max = 20)
    private String lastname;

    @NotBlank
    @Size(min = 6, max = 20)
    private String username;

    @NotBlank
    @Size(min = 5, max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Card> cards;

    private Date createdAt;
    private Date updatedAt;

    public User() {
    }

    public User(String name, String lastname, String username, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
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
