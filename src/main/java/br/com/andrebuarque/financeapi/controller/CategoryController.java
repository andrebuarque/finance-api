package br.com.andrebuarque.financeapi.controller;

import br.com.andrebuarque.financeapi.dto.CategoryDto;
import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.exception.CategoryNotFoundException;
import br.com.andrebuarque.financeapi.exception.InvalidUserException;
import br.com.andrebuarque.financeapi.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@RolesAllowed({"user"})
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Stream<Category> findAll(@RequestAttribute User loggedUser) {
        return categoryService.findAll(loggedUser);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody @Valid CategoryDto categoryDto, @RequestAttribute User loggedUser) {
        return categoryService.create(loggedUser, categoryDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Category update(@PathVariable String id, @RequestAttribute User loggedUser,
                           @RequestBody @Valid CategoryDto categoryDto) throws CategoryNotFoundException, InvalidUserException {
        return categoryService.update(loggedUser, id, categoryDto);
    }

    @GetMapping("/{id}")
    public Category findById(@PathVariable String id, @RequestAttribute User loggedUser) throws CategoryNotFoundException, InvalidUserException {
        return categoryService.findById(loggedUser, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id, @RequestAttribute User loggedUser) throws CategoryNotFoundException, InvalidUserException {
        categoryService.deleteById(loggedUser, id);
    }
}
