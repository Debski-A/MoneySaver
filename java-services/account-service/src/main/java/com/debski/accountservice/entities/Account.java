package com.debski.accountservice.entities;

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

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
            name = "accounts_roles",
            joinColumns = { @JoinColumn(name = "id_account") },
            inverseJoinColumns = { @JoinColumn(name = "id_role") }
    )
    private Set<Role> roles;

}
