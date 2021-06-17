package br.com.andrebuarque.financeapi.service;

import br.com.andrebuarque.financeapi.dto.CategoryDto;
import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.exception.CategoryNotFoundException;
import br.com.andrebuarque.financeapi.exception.InvalidUserException;
import br.com.andrebuarque.financeapi.mapper.CategoryMapper;
import br.com.andrebuarque.financeapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

import static br.com.andrebuarque.financeapi.validation.BeanValidator.validate;

@Service
public class CategoryService {
    private static final CategoryMapper MAPPER = CategoryMapper.INSTANCE;
    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Stream<Category> findAll(final User user) {
        return categoryRepository.findByUser(user);
    }

    public Category create(final User user, final CategoryDto categoryDto) {
        validate(categoryDto);

        final Category category = MAPPER.toEntity(categoryDto);
        category.setUser(user);

        return categoryRepository.save(category);
    }

    public Category update(final User user, final String categoryId, final CategoryDto categoryDto) throws CategoryNotFoundException, InvalidUserException {
        assert Objects.nonNull(categoryId);
        validate(categoryDto);

        Category category = findById(categoryId);
        validateSameUserFromCategory(user, category);

        final Category newCategory = MAPPER.toEntity(categoryDto);
        newCategory.setUser(user);
        newCategory.setId(categoryId);

        return categoryRepository.save(newCategory);
    }

    public Category findById(final User user, final String categoryId) throws CategoryNotFoundException, InvalidUserException {
        final Category category = findById(categoryId);
        validateSameUserFromCategory(user, category);
        return category;
    }

    public void deleteById(final User user, final String categoryId) throws CategoryNotFoundException, InvalidUserException {
        findById(user, categoryId);
        categoryRepository.deleteById(categoryId);
    }

    private void validateSameUserFromCategory(final User user, final Category category) throws InvalidUserException {
        if (!user.getId().equals(category.getUser().getId())) {
            throw new InvalidUserException();
        }
    }

    private Category findById(final String categoryId) throws CategoryNotFoundException {
        return categoryRepository
            .findById(categoryId)
            .orElseThrow(CategoryNotFoundException::new);
    }
}
