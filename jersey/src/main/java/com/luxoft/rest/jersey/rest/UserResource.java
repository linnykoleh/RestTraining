package com.luxoft.rest.jersey.rest;

import com.luxoft.rest.domain.UserRepository;
import com.luxoft.rest.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * @author Anton German
 * @since 02 September 2016
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("/users")
public class UserResource {
    @Autowired
    private UserRepository userRepository;

    @GET
    public Collection<User> getUsers() {
        return userRepository.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(User user) {
        userRepository.save(user);
    }

    @DELETE
    @Path("/{id}")
    public void deleteUser(@PathParam("id") long id) {
        userRepository.delete(id);
    }

}
