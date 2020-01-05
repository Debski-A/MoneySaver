package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.Income;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends CrudRepository<Income, Long> {

}
