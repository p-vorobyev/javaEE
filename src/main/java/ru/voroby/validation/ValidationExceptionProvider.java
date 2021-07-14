package ru.voroby.validation;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionProvider implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException ex) {
        Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
        ex.getConstraintViolations().forEach(e ->
                builder.header("Error description", e.getMessage()));

        return builder.build();
    }

}
