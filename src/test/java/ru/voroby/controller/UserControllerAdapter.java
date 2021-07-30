package ru.voroby.controller;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.voroby.entity.User;
import ru.voroby.repository.UserStore;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class UserControllerAdapter extends WireMock {

    @Mock
    private UserStore userStore;

    @Mock
    private UriInfo uriInfo;

    @Mock
    private UriBuilder uriBuilder;

    @InjectMocks
    UserController controller;

    String URL = "http://localhost:8089/users";

    private int sequence = 100;

    void whenAddUser(User user) {
        when(userStore.addUser(user)).thenReturn(sequence++);
        //when(uriInfo.getBaseUriBuilder().path(UserController.class).path(UserController.class, "getUser").build(anyInt())).thenReturn(any());
        //UriBuilder uriBuilder = UriBuilder.fromPath(URL);
        //when(uriBuilder.path(anyString())).thenReturn(any());
        //Response response = controller.addUser(user);
        stubFor(
                post("/users").withHeader("Content-Type", containing(APPLICATION_JSON)
                ).willReturn(
                      ResponseDefinitionBuilder.responseDefinition().withStatus(200).withBody("It's working!")
                ));
    }
}
