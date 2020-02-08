package com.debski.calculationservice.clients;

import com.debski.calculationservice.models.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service", fallback = AccountServiceClientFallback.class)
public interface AccountServiceClient {

//    Oauth2 blokuje feign: https://itnext.io/microservices-with-spring-boot-and-spring-cloud-441e3dabc67d
    @GetMapping(value = "/accounts/get/{username}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccountDTO getAccountData(@PathVariable("username") String username);
}
