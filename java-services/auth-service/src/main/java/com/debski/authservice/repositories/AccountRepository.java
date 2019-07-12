package com.debski.authservice.repositories;

import com.debski.authservice.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByUsername(@Param("username") String username);
}
