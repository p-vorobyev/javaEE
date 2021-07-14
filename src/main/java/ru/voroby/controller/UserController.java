package ru.voroby.controller;

import ru.voroby.entity.User;
import ru.voroby.repository.UserStore;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
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
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserStore userStore;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonArray getUsers() {
        return userStore.getAllUsers().stream()
                .map(this::buildUserJson)
                .collect(JsonCollectors.toJsonArray());
    }

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") Integer id) {
        return userStore.getUser(id)
                .map(user -> Response.ok().entity(buildUserJson(user)).build())
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

    private JsonObject buildUserJson(User user) {
        return Json.createObjectBuilder()
                .add("id", user.getId())
                .add("name", user.getName())
                .add("age", user.getAge()).build();
    }

}
