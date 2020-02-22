package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.Outcome;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OutcomeRepository extends CrudRepository<Outcome, Long> {

    Long removeByUuid(UUID uuid);
}
