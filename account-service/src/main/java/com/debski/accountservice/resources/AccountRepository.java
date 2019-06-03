package com.debski.accountservice.resources;

import com.debski.accountservice.entities.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

//TODO add spring security

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    List<Account> findByUsername(@Param("username") String username);
}
