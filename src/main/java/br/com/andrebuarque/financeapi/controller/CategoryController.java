package br.com.andrebuarque.financeapi.controller;

import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
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
}
