package org.money.back.domain.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountNotFoundException extends RuntimeException implements ExceptionMapper<AccountNotFoundException> {

    public AccountNotFoundException(String accountId) {
        super("Account with accountId = " + accountId + " not found.");
    }

    public AccountNotFoundException() {
    }

    @Override
    public Response toResponse(AccountNotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
