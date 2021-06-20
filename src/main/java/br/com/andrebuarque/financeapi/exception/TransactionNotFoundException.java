package br.com.andrebuarque.financeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends Exception {
    public TransactionNotFoundException() {
        super("Transaction not found");
    }
}
