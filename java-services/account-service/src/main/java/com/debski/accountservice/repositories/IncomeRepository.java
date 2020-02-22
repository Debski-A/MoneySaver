package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.Income;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IncomeRepository extends CrudRepository<Income, Long> {

    Long removeByUuid(UUID uuid);
}
