package com.debski.accountservice.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Account extends BaseEntity {

    private String username;

    private String password;

    private String email;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "accounts_roles", joinColumns = @JoinColumn(name = "id_account"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

}
