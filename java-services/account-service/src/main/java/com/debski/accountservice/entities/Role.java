package com.debski.accountservice.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@EqualsAndHashCode(exclude = "accounts")
@NoArgsConstructor
public class Role {

    public Role(RoleTypes type) {
        this.id = type.getId();
        this.role = type.getRoleName();
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String role;

    @ManyToMany(mappedBy = "roles")
    @Getter
    private Set<Account> accounts;

}
