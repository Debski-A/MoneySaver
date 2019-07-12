package com.debski.authservice.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Account extends BaseEntity {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "id_account"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public Account(Account account) {
        this.id = account.id;
        this.uuid = account.uuid;
        this.username = account.username;
        this.password = account.password;
        this.email = account.email;
        this.roles = account.roles;
    }
}
