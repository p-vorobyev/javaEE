package ru.voroby.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestController {

    @GET
    public List<String> test() {
        return List.of("first", "second", "third");
    }

}
