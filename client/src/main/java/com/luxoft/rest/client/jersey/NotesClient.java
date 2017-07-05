package com.luxoft.rest.client.jersey;

import com.luxoft.rest.domain.Note;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * @author Anton German
 * @since 08 September 2016
 */
public class NotesClient {
    final MediaType mediaType;

    public NotesClient(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    protected void processGetResponse(Response response) {
        try {
            GenericType<Collection<Note>> notesGenericType = new GenericType<Collection<Note>>() {};
            Collection<Note> notes = response.readEntity(notesGenericType);
            for (Note note : notes) {
                System.out.println(String.format("%1$s -> %2$s", note.getId(), note.getText()));
            }
        } finally {
            response.close();
        }
    }

    protected void processPostResponse(Response response) {
        response.close();
    }

    protected void listNotes() {
        System.out.println("---");

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
        clientConfig.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget webTarget = client.target("http://localhost:8080/rs").path("/users/user1/notes");
        Response response = webTarget.request(mediaType).get();

        processGetResponse(response);
    }

    protected void addNote() {
        Note note = new Note();
        note.setText("The new added note.");

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
        clientConfig.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget webTarget = client.target("http://localhost:8080/rs").path("/users/user1/notes");
        Response response = webTarget.request(mediaType).post(Entity.entity(note, mediaType));

        processPostResponse(response);
    }

    public static void main(String[] args) {
//        NotesClient me = new NotesClient(MediaType.APPLICATION_XML_TYPE);
        NotesClient me = new NotesClient(MediaType.APPLICATION_JSON_TYPE);
        me.listNotes();
        me.addNote();
        me.listNotes();
    }
}
