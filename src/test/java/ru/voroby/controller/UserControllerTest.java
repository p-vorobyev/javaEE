package ru.voroby.controller;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.junit.AfterClass;
import org.junit.Test;
import ru.voroby.entity.User;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.apache.hc.core5.http.ContentType.APPLICATION_JSON;
import static ru.voroby.controller.HttpUtils.post;

public class UserControllerTest extends UserControllerAdapter {

    static final CloseableHttpClient client = HttpClients.createDefault();

    @Test
    public void addUser() throws URISyntaxException, IOException {
        whenAddUser(new User());
        try (CloseableHttpResponse execute = client.execute(post(URL, new BasicHeader("Content-Type", APPLICATION_JSON)))) {
            HttpEntity entity = execute.getEntity();
            String s = new String(entity.getContent().readAllBytes());
            System.out.println(s);
        }
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        client.close();
    }
}
