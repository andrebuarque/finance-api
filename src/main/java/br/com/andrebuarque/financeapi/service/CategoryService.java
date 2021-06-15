package br.com.andrebuarque.financeapi.service;

import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Stream<Category> findAll(final User user) {
        return categoryRepository.findByUser(user);
    }
}
