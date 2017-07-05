package com.luxoft.rest.hateoas.domain;

import com.luxoft.rest.hateoas.domain.entities.Account;
import com.luxoft.rest.hateoas.domain.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Anton German
 * @since 07 September 2016
 */
public class InitialDataLoader {
    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void load() {
        Account account = new Account();
        account.setBalance(50);
        accountRepository.save(account);

        account = new Account();
        account.setBalance(100);
        accountRepository.save(account);

        account = new Account();
        account.setBalance(200);
        accountRepository.save(account);
    }
}
