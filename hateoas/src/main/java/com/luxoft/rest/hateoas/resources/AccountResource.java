package com.luxoft.rest.hateoas.resources;

import com.luxoft.rest.hateoas.domain.entities.Account;
import com.luxoft.rest.hateoas.domain.entities.ActionLink;
import com.luxoft.rest.hateoas.domain.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * @author Anton German
 * @since 09 September 2016
 */
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    @Autowired
    private AccountRepository accountRepository;

    @GET
    public Collection<Account> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        accounts.forEach(AccountResource::supplyActionLinks);
        return accounts;
    }

    @GET
    @Path("/{id}")
    public Account getAccount(@PathParam("id") long id) {
        Account account = accountRepository.findOne(id);
        if (account == null) {
            throw new WebApplicationException("Account not found: id = " + id, 404);
        }
        supplyActionLinks(account);
        return account;
    }

    @GET
    @Path("/{id}/withdraw/{amount}")
    public void withdraw(@PathParam("id") long id, @PathParam("amount") double amount) {
        Account account = getAccount(id);
        if (amount > account.getBalance()) {
            throw new WebApplicationException("Not enough money.", 404);
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @GET
    @Path("/{id}/deposit/{amount}")
    public void deposit(@PathParam("id") long id, @PathParam("amount") double amount) {
        Account account = getAccount(id);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    private static void supplyActionLinks(Account account) {
        URI depositLink = UriBuilder.fromResource(AccountResource.class)
                .path(AccountResource.class, "deposit").build(account.getId(), 50);
        account.getActionLinks().add(ActionLink.create("deposit", depositLink));

        if (account.getBalance() > 0) {
            URI withdrawLink = UriBuilder.fromResource(AccountResource.class)
                    .path(AccountResource.class, "withdraw").build(account.getId(), 50);
            account.getActionLinks().add(ActionLink.create("withdraw", withdrawLink));
        }
    }
}
