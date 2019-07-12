package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByUsername(String username);
    boolean existsByUsername(String username);
}
