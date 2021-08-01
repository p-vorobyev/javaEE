package ru.voroby.controller;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import org.apache.hc.core5.http.HttpStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.voroby.entity.User;
import ru.voroby.repository.UserStore;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.when;

public abstract class UserControllerAdapter extends WireMock {

    @Mock
    private UserStore userStore;

    @Mock
    private UriInfo uriInfo;

    @InjectMocks
    private UserController controller;

    String URL = "http://localhost:8089/users";

    static int sequence = 100;

    static User initialUser;

    static Map<Integer, User> inMem = new HashMap<>();

    static {
        initialUser = new User("Viktoria", 27);
        initialUser.setId(sequence);
        inMem.put(sequence, initialUser);
    }

    void whenAddUser(User user) {
        when(userStore.addUser(user)).thenReturn(sequence++);
        var uriBuilder = UriBuilder.fromPath(URL);
        when(uriInfo.getBaseUriBuilder()).thenReturn(uriBuilder);
        user.setId(sequence);
        inMem.put(user.getId(), user);
        Response response = controller.addUser(user);
        stubFor(
                post("/users").withHeader("Content-Type", containing(APPLICATION_JSON))
                        .willReturn(
                                ResponseDefinitionBuilder.responseDefinition()
                                        .withStatus(response.getStatus())
                                        .withHeader("Location", URL + "/" + sequence)
                        ));
    }

    void whenGetUser(int id) {
        User u = inMem.get(id);
        when(userStore.getUser(id)).thenReturn(Optional.ofNullable(u));
        Response response = controller.getUser(id);
        stubFor(
                get("/users/" + id)
                        .willReturn(
                                ResponseDefinitionBuilder.responseDefinition()
                                        .withStatus(response.getStatus())
                                        .withHeader("Content-Type", APPLICATION_JSON)
                                        .withBody(u != null ? json(u) : "")
                        )
        );
    }

    void whenGetUsers() {
        when(userStore.getAllUsers()).thenReturn(new ArrayList<>(inMem.values()));
        JsonArray users = controller.getUsers();
        stubFor(
                get("/users")
                        .willReturn(
                                ResponseDefinitionBuilder.responseDefinition()
                                        .withStatus(HttpStatus.SC_OK)
                                        .withHeader("Content-Type", APPLICATION_JSON)
                                        .withBody(json(users))
                        )
        );
    }

    private String json(JsonArray users) {
        var writer = new StringWriter();
        Json.createWriter(writer).writeArray(users);
        return writer.toString();
    }

    String json(User u) {
        var writer = new StringWriter();
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("id", u.getId())
                .add("name", u.getName())
                .add("age", u.getAge()).build();
        Json.createWriter(writer).writeObject(jsonObject);

        return writer.toString();
    }

    User fromJson(String json) {
        var reader = new StringReader(json);
        JsonObject jsonObject = Json.createReader(reader).readObject();
        var u = new User(jsonObject.getString("name"), jsonObject.getInt("age"));
        u.setId(jsonObject.getInt("id"));

        return  u;
    }

    List<User> fromJsonArray(String json) {
        var reader = new StringReader(json);
        JsonArray jsons = Json.createReader(reader).readArray();
        List<User> users = new ArrayList<>();
        jsons.forEach(value -> {
            JsonObject jsonObj = value.asJsonObject();
            var u = new User(jsonObj.getString("name"), jsonObj.getInt("age"));
            u.setId(jsonObj.getInt("id"));
            users.add(u);
        });

        return users;
    }
}
