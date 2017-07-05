package com.luxoft.rest.client.jersey;

import com.luxoft.rest.domain.Note;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Future;

/**
 * @author Anton German
 * @since 08 September 2016
 */
public class NotesClientAsync extends NotesClient {
    private static final Logger logger = LogManager.getLogger(NotesClientAsync.class);

    public NotesClientAsync(MediaType mediaType) {
        super(mediaType);
    }

    @Override
    protected void listNotes() {
        System.out.println("---");

        InvocationCallback<Response> invocationCallback = new InvocationCallback<Response>() {
            public void completed(Response response) {
                processGetResponse(response);
            }
            public void failed(Throwable throwable) {
                logger.error(throwable);
            }
        };

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
        clientConfig.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget webTarget = client.target("http://localhost:8080/rs").path("/users/user1/notes");
        Future<Response> responseFuture = webTarget.request(mediaType).async().get(invocationCallback);
        waitForFuture(responseFuture);
    }

    @Override
    protected void addNote() {
        Note note = new Note();
        note.setText("The new added note.");

        InvocationCallback<Response> invocationCallback = new InvocationCallback<Response>() {
            public void completed(Response response) {
                processPostResponse(response);
            }
            public void failed(Throwable throwable) {
                logger.error(throwable);
            }
        };

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
        clientConfig.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget webTarget = client.target("http://localhost:8080/rs").path("/users/user1/notes");
        Future<Response> responseFuture = webTarget.request(mediaType).async().post(
                Entity.entity(note, mediaType),
                invocationCallback);
        waitForFuture(responseFuture);
    }

    private static void waitForFuture(Future<?> future) {
        while (!future.isDone()) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }

    public static void main(String[] args) {
//        NotesClientAsync me = new NotesClientAsync(MediaType.APPLICATION_XML_TYPE);
        NotesClientAsync me = new NotesClientAsync(MediaType.APPLICATION_JSON_TYPE);
        me.listNotes();
        me.addNote();
        me.listNotes();
    }
}
