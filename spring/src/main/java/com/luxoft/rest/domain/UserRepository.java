package com.luxoft.rest.domain;

import com.luxoft.rest.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Anton German
 * @since 31 August 2016
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
