package ru.voroby.controller;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.message.BasicHeader;
import org.junit.AfterClass;
import org.junit.Test;
import ru.voroby.entity.User;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.hc.core5.http.ContentType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.voroby.controller.utils.HttpUtils.get;
import static ru.voroby.controller.utils.HttpUtils.post;

public class UserControllerTest extends UserControllerAdapter {

    static final CloseableHttpClient client = HttpClients.createDefault();

    @Test
    public void addUser() throws URISyntaxException, IOException, ProtocolException {
        whenAddUser(new User("Martin", 25));
        try (CloseableHttpResponse execute = client.execute(post(URL, new BasicHeader("Content-Type", APPLICATION_JSON)))) {
            Header location = execute.getHeader("Location");
            assertEquals(201, execute.getCode());
            assertNotNull(location);
            assertTrue(location.getValue().contains(URL));
        }
    }

    @Test
    public void getUser() throws IOException {
        whenGetUser(initialUser.getId());
        try (CloseableHttpResponse execute = client.execute(get(URL + "/" + initialUser.getId()))) {
            int code = execute.getCode();
            if (code == 200) {
                HttpEntity entity = execute.getEntity();
                User user = fromJson(new String(entity.getContent().readAllBytes()));
                assertEquals(user, initialUser);
            } else {
                assertEquals(204, code);
            }
        }
    }

    @Test
    public void getUsers() throws IOException {
        whenGetUsers();
        try (CloseableHttpResponse execute = client.execute(get(URL))) {
            assertEquals(200, execute.getCode());
            List<User> users = fromJsonArray(new String(execute.getEntity().getContent().readAllBytes()));
            assertEquals(new ArrayList<>(inMem.values()), users);
        }
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        client.close();
    }
}
