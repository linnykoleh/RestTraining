package com.luxoft.rest.domain;

import com.luxoft.rest.domain.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * @author Anton German
 * @since 31 August 2016
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
    Collection<Note> findByUserUsername(String username);
}
