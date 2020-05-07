package com.klever.bok.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.klever.bok.models.audit.UserDateAudit;
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
@JsonView(Views.UserAccess.class)
public class User extends UserDateAudit {

    @Id
    private UUID id;

    private String name;

    private String lastname;

    private String username;

    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

//    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL)
//    private Set<Card> cards;

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
    }
}
