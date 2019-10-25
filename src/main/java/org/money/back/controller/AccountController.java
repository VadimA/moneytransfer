package org.money.back.controller;

import org.money.back.dto.AccountDTO;
import org.money.back.mapper.AccountMapper;
import org.money.back.service.AccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/accounts")
public class AccountController {

    private final AccountService accountService = AccountService.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts(){
        return Response.ok(accountService.getAllAccounts()
                .stream()
                .map(AccountMapper::toAccountDTO)
        .collect(Collectors.toList())).build();
    }

    @GET
    @Path("{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("accountId") String accountId){
        return Response.ok(AccountMapper.toAccountDTO(accountService.getAccountById(accountId))).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountDTO account) {
        return Response.ok(accountService.addNewAccount(AccountMapper.toAccount(account))).status(201).build();
    }
}
