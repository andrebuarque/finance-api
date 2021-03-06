package br.com.andrebuarque.financeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException() {
        super("Category not found");
    }
}
