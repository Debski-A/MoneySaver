package com.debski.calculationservice.clients;

import com.debski.calculationservice.models.AccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountServiceClientFallback implements AccountServiceClient{

    @Override
    public AccountDTO getAccountData(String username) {
        log.error("Couldn't connect with account-service");
        return null;
    }
}
