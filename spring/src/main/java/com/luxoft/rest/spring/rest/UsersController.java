package com.luxoft.rest.spring.rest;

import com.luxoft.rest.domain.UserRepository;
import com.luxoft.rest.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author Anton German
 * @since 01 September 2016
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<User> getUsers() {
        return userRepository.getUsers();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addUser(@RequestBody User user) {
        userRepository.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
    }
}
