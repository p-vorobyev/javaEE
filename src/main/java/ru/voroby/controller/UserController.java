package ru.voroby.controller;

import ru.voroby.entity.User;
import ru.voroby.repository.UserStore;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserStore userStore;

    @Context
    UriInfo uriInfo;

    @GET
    public List<User> getUsers() {
        return userStore.getAllUsers();
    }

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") Integer id) {
        return userStore.getUser(id)
                .map(userRecord -> Response.ok().entity(userRecord).build())
                .orElseGet(() -> Response.noContent().build());
    }

    @POST
    public Response addUser(User dto) {
        int id = userStore.addUser(dto);
        URI uri = uriInfo.getBaseUriBuilder()
                .path(UserController.class)
                .path(UserController.class, "getUser")
                .build(id);

        return Response.created(uri).build();
    }

}
