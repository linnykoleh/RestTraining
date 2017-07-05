package com.luxoft.rest.client.httpclient;

import com.luxoft.rest.domain.Note;
import com.luxoft.rest.domain.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;

/**
 * @author Anton German
 * @since 08 September 2016
 */
public class NotesClient {
    private static final String SERVICE_URL = "http://localhost:8080/rs/users/user1/notes";
    private static final JAXBContext JAXB_CONTEXT;
    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(Note.class, User.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processGetResponse(HttpResponse response) throws Exception {
        Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();

        // Cannot unmarshal to collection directly because Moxy (used on server side by Jersey) adds the wrapping
        // element (<notes>) that's absent in domain model.
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(response.getEntity().getContent());

        NodeList nodesList = document.getFirstChild().getChildNodes();
        for (int i = 0; i < nodesList.getLength(); i++) {
            Node node = nodesList.item(i);
            @SuppressWarnings("unchecked")
            Note note = (Note) unmarshaller.unmarshal(node);
            System.out.println(String.format("%1$s -> %2$s", note.getId(), note.getText()));
        }
    }

    private static void listNotes() throws Exception {
        System.out.println("---");
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(SERVICE_URL);
            httpGet.addHeader("Accept", "application/xml");
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                processGetResponse(response);
            }
        }
    }

    private static void addNote() throws Exception {
        Note note = new Note();
        note.setText("The new added note.");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
        marshaller.marshal(note, os);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(SERVICE_URL);
            HttpEntity httpEntity = new ByteArrayEntity(os.toByteArray(), ContentType.APPLICATION_XML);
            httpPost.setEntity(httpEntity);
            httpClient.execute(httpPost).close();
        }
    }

    public static void main(String[] args) throws Exception {
        listNotes();
        addNote();
        listNotes();
    }
}