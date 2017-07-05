package com.luxoft.rest.jersey.rest.async;

import com.luxoft.rest.domain.NoteRepository;
import com.luxoft.rest.domain.UserRepository;
import com.luxoft.rest.domain.entities.Note;
import com.luxoft.rest.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executor;

/**
 * Note: org.glassfish.jersey.servlet.ServletContainer (see web.xml) must be configured with async-supported=true
 *
 * @author Anton German
 * @since 31 August 2016
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("/async/users/{username}/notes")
public class NoteResourceAsync {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Executor executor;

    @GET
    public void readNotes(@PathParam("username") String username, @Suspended final AsyncResponse asyncResponse) {
        Runnable runnable = () -> {

            // Do not return just the Collection<Note> because of "SEVERE: MessageBodyWriter not found for media type=application/json type=class java.util.arraylist"
            // The problem is that generic information about the type of the list is being erased since java lists implement java.util.Collection interface.
            // JAX-RS has a standard solution for this. It has a wrapper class javax.ws.rs.core.GenericEntity that can preserve the list type.

            Collection<Note> notes = noteRepository.findByUserUsername(username);
            GenericEntity<Collection<Note>> response = new GenericEntity<Collection<Note>>(notes) {};
            asyncResponse.resume(response);
        };
        executor.execute(runnable);

//        or
//
//        CompletableFuture
//                .supplyAsync(() -> noteRepository.findByUserUsername(username))
//                .thenAccept((notes) -> asyncResponse.resume(new GenericEntity<Collection<Note>>(notes) {}));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addNote(@PathParam("username") String username, Note note, @Suspended final AsyncResponse asyncResponse) {
        Runnable runnable = () -> {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                note.setUser(user.get());
                noteRepository.save(note);
                asyncResponse.resume(Response.ok().build());
            } else {
                asyncResponse.resume(new WebApplicationException("User not found: " + username, 404));
            }
        };
        executor.execute(runnable);
    }
}
