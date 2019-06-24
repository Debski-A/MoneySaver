package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//TODO add spring security
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByUsername(@Param("username") String username);
    boolean existsByUsername(@Param("username") String username);
}
