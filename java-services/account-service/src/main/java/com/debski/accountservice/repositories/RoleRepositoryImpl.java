package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.Role;
import com.debski.accountservice.entities.RoleTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public Role getRole(RoleTypes type) {
        return entityManager.find(Role.class, type.getId());
    }
}
