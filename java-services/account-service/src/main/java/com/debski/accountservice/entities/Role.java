package com.debski.accountservice.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role {

    private Role(RoleTypes roleType) {
        this.role = roleType.type;
        this.id = roleType.id;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String role;

    @ManyToMany(mappedBy = "roles")
    @Getter
    private Set<Account> account;

    public static Role getSpecificRole(RoleTypes type) {
        switch (type) {
            case USER: return new Role(RoleTypes.USER);
            case PREMIUM: return new Role(RoleTypes.PREMIUM);
            default: return null;
        }
    }
}
