package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.Outcome;
import com.debski.accountservice.entities.OutcomeCategory;
import com.debski.accountservice.entities.OutcomeCategoryTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class OutcomeRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public Outcome findById(Long id) {
        return entityManager.find(Outcome.class, id);
    }

    public OutcomeCategory getOutcomeCategory(OutcomeCategoryTypes type) {
        return entityManager.find(OutcomeCategory.class, type.getId());
    }
}
