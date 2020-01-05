package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class BudgetRepositoryImpl implements BudgetRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private <T> List<T> getAllQuery(Class<T> clazz){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public List<IncomeCategory> getAllIncomeCategories() {
        return getAllQuery(IncomeCategory.class);
    }

    @Override
    public List<OutcomeCategory> getAllOutcomeCategories() {
        return getAllQuery(OutcomeCategory.class);
    }

    // methods for test use cases
    public Income findIncomeById(Long id) {
        return entityManager.find(Income.class, id);
    }

    public IncomeCategory getIncomeCategory(IncomeCategoryTypes type) {
        return entityManager.find(IncomeCategory.class, type.getId());
    }

    public Outcome findOutcomeById(Long id) {
        return entityManager.find(Outcome.class, id);
    }

    public OutcomeCategory getOutcomeCategory(OutcomeCategoryTypes type) {
        return entityManager.find(OutcomeCategory.class, type.getId());
    }

}
