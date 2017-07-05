package com.luxoft.rest.jersey.rest;

import com.luxoft.rest.domain.NoteRepository;
import com.luxoft.rest.domain.UserRepository;
import com.luxoft.rest.domain.entities.Note;
import com.luxoft.rest.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * @author Anton German
 * @since 31 August 2016
 */
@Path("/users/{username}/notes")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class NoteResource {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;

    @GET
    public Collection<Note> getNotes(@PathParam("username") String username) {
        return noteRepository.findByUserUsername(username);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void addNote(@PathParam("username") String username, Note note) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new WebApplicationException("User not found: " + username, 404));
        note.setUser(user);
        noteRepository.save(note);
    }

    @DELETE
    @Path("/{id}")
    public void deleteNote(@PathParam("id") long id) {
        noteRepository.delete(id);
    }
}
