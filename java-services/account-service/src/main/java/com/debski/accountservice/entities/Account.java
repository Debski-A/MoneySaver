package com.debski.accountservice.entities;

import com.debski.accountservice.entities.enums.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {

    @NotBlank
    @Column(unique = true)
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

    @NotNull
    @Enumerated
    private Role role;

}
