package org.money.back.domain.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InsufficientBalanceException extends RuntimeException implements ExceptionMapper<InsufficientBalanceException> {

    public InsufficientBalanceException() {
    }

    public InsufficientBalanceException(String accountId) {
        super("Transaction can not be performed due to lack of funds on the account " + accountId);
    }

    @Override
    public Response toResponse(InsufficientBalanceException exception) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}