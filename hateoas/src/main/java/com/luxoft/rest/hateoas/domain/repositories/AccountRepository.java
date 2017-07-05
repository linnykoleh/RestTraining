package com.luxoft.rest.hateoas.domain.repositories;

import com.luxoft.rest.hateoas.domain.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Anton German
 * @since 31 August 2016
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
}
