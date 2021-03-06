package ru.voroby.controller;

import lombok.extern.slf4j.Slf4j;
import ru.voroby.entity.User;
import ru.voroby.repository.UserStore;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Slf4j
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @EJB
    private UserStore userStore;

    @Context
    UriInfo uriInfo;

    @Context
    SecurityContext securityContext;

    @GET
    public JsonArray getUsers() {
        log.info("Endpoint getUsers() invoked: [username: {}]", getUsername());
        return userStore.getAllUsers().stream()
                .map(this::buildUserJson)
                .collect(JsonCollectors.toJsonArray());
    }

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") Integer id) {
        log.info("Endpoint getUser(id) invoked: [username: {}]", getUsername());
        return userStore.getUser(id)
                .map(user -> Response.ok().entity(buildUserJson(user)).build())
                .orElseGet(() -> Response.noContent().build());
    }

    @POST
    public Response addUser(@Valid @NotNull User user) {
        log.info("Endpoint addUser(user) invoked: [username: {}]", getUsername());
        int id = userStore.addUser(user);
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

    private String getUsername() {
        return securityContext.getUserPrincipal().getName();
    }

}
