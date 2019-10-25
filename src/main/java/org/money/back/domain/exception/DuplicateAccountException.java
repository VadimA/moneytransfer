package org.money.back.domain.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DuplicateAccountException extends RuntimeException implements ExceptionMapper<DuplicateAccountException> {

    public DuplicateAccountException(String email) {
        super("Account with email:" + email + " already exists.");
    }

    public DuplicateAccountException() {
    }

    @Override
    public Response toResponse(DuplicateAccountException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
