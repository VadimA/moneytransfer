package org.money.back.controller;

import org.money.back.dto.TransactionDTO;
import org.money.back.mapper.TransactionMapper;
import org.money.back.service.TransactionService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/transactions")
public class TransactionController {

    private final TransactionService transactionService = TransactionService.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTransactions() {
        return Response.ok(transactionService.getAllTransactions()
                .stream()
                .map(TransactionMapper::toTransactionDTO)
                .collect(Collectors.toList())).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewTransaction (TransactionDTO transactionDTO) {
        return Response.ok(transactionService.createNewTransaction(TransactionMapper.toTransaction(transactionDTO)))
                .status(201).build();
    }

    @GET
    @Path("{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionById(@PathParam("transactionId") String transactionId) {
        return Response.ok(TransactionMapper.toTransactionDTO(transactionService.getTransactionById(transactionId)))
                .build();
    }
}
