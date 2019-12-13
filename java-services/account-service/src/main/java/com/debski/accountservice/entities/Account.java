package com.debski.accountservice.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Boolean enabled;

    @Email
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
    private Set<Income> incomes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true)
    private Set<Outcome> outcomes;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
            name = "accounts_roles",
            joinColumns = { @JoinColumn(name = "id_account") },
            inverseJoinColumns = { @JoinColumn(name = "id_role") }
    )
    private Set<Role> roles;

}
