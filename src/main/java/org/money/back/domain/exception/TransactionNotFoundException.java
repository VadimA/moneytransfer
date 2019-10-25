package org.money.back.domain.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TransactionNotFoundException extends RuntimeException implements ExceptionMapper<TransactionNotFoundException> {

    public TransactionNotFoundException(String transactionId) {
        super("Transaction with id = " + transactionId + " not found.");
    }

    public TransactionNotFoundException() {
    }

    @Override
    public Response toResponse(TransactionNotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
