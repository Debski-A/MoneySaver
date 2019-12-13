package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.IncomeCategory;
import com.debski.accountservice.entities.IncomeCategoryTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class IncomeRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public Income findById(Long id) {
        return entityManager.find(Income.class, id);
    }

    public IncomeCategory getIncomeCategory(IncomeCategoryTypes type) {
        return entityManager.find(IncomeCategory.class, type.getId());
    }
}
