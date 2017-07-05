package com.luxoft.rest.domain;

import com.luxoft.rest.domain.entities.Note;
import com.luxoft.rest.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Anton German
 * @since 07 September 2016
 */
public class InitialDataLoader {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void load() {
        User user = new User();
        user.setUsername("user1");
        userRepository.save(user);

        Note note = new Note();
        note.setUser(user);
        note.setText("user1-note1");
        noteRepository.save(note);

        note = new Note();
        note.setUser(user);
        note.setText("user1-note2");
        noteRepository.save(note);

        user = new User();
        user.setUsername("user2");
        userRepository.save(user);

        note = new Note();
        note.setUser(user);
        note.setText("user2-note1");
        noteRepository.save(note);
    }
}
