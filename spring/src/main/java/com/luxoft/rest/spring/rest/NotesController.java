package com.luxoft.rest.spring.rest;

import com.luxoft.rest.domain.NoteRepository;
import com.luxoft.rest.domain.UserRepository;
import com.luxoft.rest.domain.entities.Note;
import com.luxoft.rest.domain.entities.User;
import com.luxoft.rest.spring.rest.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author Anton German
 * @since 31 August 2016
 */
@RestController
@RequestMapping("/users/{username}/notes")
public class NotesController {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Note> getNotes(@PathVariable String username) {
        return noteRepository.findByUserUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST,consumes = "application/json", produces = "application/json")
    public void addNote(@PathVariable String username, @RequestBody Note note) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        note.setUser(user);
        noteRepository.save(note);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteNote(@PathVariable long id) {
        noteRepository.delete(id);
    }
}
