package br.com.andrebuarque.financeapi.controller;

import br.com.andrebuarque.financeapi.dto.TransactionDto;
import br.com.andrebuarque.financeapi.entity.Transaction;
import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.exception.CategoryNotFoundException;
import br.com.andrebuarque.financeapi.exception.InvalidUserException;
import br.com.andrebuarque.financeapi.exception.TransactionNotFoundException;
import br.com.andrebuarque.financeapi.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
@RolesAllowed({"user"})
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public Stream<Transaction> findAll(@RequestAttribute User loggedUser) {
        return transactionService.findAll(loggedUser);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@RequestBody @Valid TransactionDto transactionDto, @RequestAttribute User loggedUser) throws CategoryNotFoundException, InvalidUserException {
        return transactionService.create(loggedUser, transactionDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Transaction update(@PathVariable String id, @RequestAttribute User loggedUser,
                           @RequestBody @Valid TransactionDto transactionDto) throws CategoryNotFoundException, InvalidUserException, TransactionNotFoundException {
        return transactionService.update(loggedUser, id, transactionDto);
    }

    @GetMapping("/{id}")
    public Transaction findById(@PathVariable String id, @RequestAttribute User loggedUser) throws InvalidUserException, TransactionNotFoundException {
        return transactionService.findById(loggedUser, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id, @RequestAttribute User loggedUser) throws InvalidUserException, TransactionNotFoundException {
        transactionService.deleteById(loggedUser, id);
    }
}
