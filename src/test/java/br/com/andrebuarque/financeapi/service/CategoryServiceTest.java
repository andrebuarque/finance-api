package br.com.andrebuarque.financeapi.service;

import br.com.andrebuarque.financeapi.dto.CategoryDto;
import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.TransactionType;
import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.exception.CategoryNotFoundException;
import br.com.andrebuarque.financeapi.exception.InvalidUserException;
import br.com.andrebuarque.financeapi.repository.CategoryRepository;
import br.com.andrebuarque.financeapi.stub.CategoryDtoStub;
import br.com.andrebuarque.financeapi.stub.CategoryStub;
import br.com.andrebuarque.financeapi.stub.UserStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @InjectMocks
    CategoryService service;

    @Mock
    CategoryRepository repository;

    @Captor
    ArgumentCaptor<Category> categoryArgumentCaptor;

    @Test
    void testFindAll() {
        final Category category = CategoryStub.getCategory();
        when(repository.findByUser(any())).thenReturn(Stream.of(category));

        final Stream<Category> result = service.findAll(new User());

        final Optional<Category> first = result.findFirst();
        assertThat(first).contains(category);
    }

    @Test
    void testCreate() {
        final User user = UserStub.getUser();
        final CategoryDto category = CategoryDtoStub.getCategory();

        service.create(user, category);

        verify(repository).save(categoryArgumentCaptor.capture());

        final Category categorySaved = categoryArgumentCaptor.getValue();

        assertThat(categorySaved.getUser()).isEqualTo(user);
        assertThat(categorySaved.getType()).isEqualTo(category.getType());
        assertThat(categorySaved.getName()).isEqualTo(category.getName());
        assertThat(categorySaved.getPattern()).isEqualTo(category.getPattern());
    }

    @Test
    void testCreateCategoryWithInvalidPayload() {
        assertThrows(ConstraintViolationException.class, () -> service.create(UserStub.getUser(), new CategoryDto()));
        verify(repository, times(0)).save(any());
    }

    @Test
    void testUpdateWithoutCategoryId() {
        assertThrows(AssertionError.class,
            () -> service.update(UserStub.getUser(), null, CategoryDtoStub.getCategory()));

        verify(repository, times(0)).findById(any());
        verify(repository, times(0)).save(any());
    }

    @Test
    void testUpdateWithInvalidCategory() {
        final String categoryId = "377492af-03c2-46e3-b3d7-f824ebf08721";

        assertThrows(ConstraintViolationException.class,
            () -> service.update(UserStub.getUser(), categoryId, new CategoryDto()));

        verify(repository, times(0)).findById(any());
        verify(repository, times(0)).save(any());
    }

    @Test
    void testUpdateUnknownCategory() {
        final String categoryId = "377492af-03c2-46e3-b3d7-f824ebf08721";

        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
            () -> service.update(UserStub.getUser(), categoryId, CategoryDtoStub.getCategory()));

        verify(repository).findById(any());
        verify(repository, times(0)).save(any());
    }

    @Test
    void testUpdateWithDifferentUser() {
        final String categoryId = "377492af-03c2-46e3-b3d7-f824ebf08721";

        final User user = UserStub.getUser();
        user.setId("123");
        final Category category = new Category();
        category.setUser(user);

        when(repository.findById(any())).thenReturn(Optional.of(category));

        assertThrows(InvalidUserException.class,
            () -> service.update(UserStub.getUser(), categoryId, CategoryDtoStub.getCategory()));
    }

    @Test
    void testUpdate() throws CategoryNotFoundException, InvalidUserException {
        final String categoryId = "377492af-03c2-46e3-b3d7-f824ebf08721";

        final User user = UserStub.getUser();

        final Category oldCategory = new Category();
        oldCategory.setId(categoryId);
        oldCategory.setName("old name");
        oldCategory.setPattern("old pattern");
        oldCategory.setType(TransactionType.OUTCOME);
        oldCategory.setUser(user);

        when(repository.findById(any())).thenReturn(Optional.of(oldCategory));

        final CategoryDto newCategory = CategoryDtoStub.getCategory();
        service.update(UserStub.getUser(), categoryId, newCategory);

        verify(repository).findById(eq(categoryId));
        verify(repository).save(categoryArgumentCaptor.capture());

        final Category categorySaved = categoryArgumentCaptor.getValue();
        assertThat(categorySaved.getId()).isEqualTo(categoryId);
        assertThat(categorySaved.getName()).isEqualTo(newCategory.getName()).isNotEqualTo(oldCategory.getName());
        assertThat(categorySaved.getType()).isEqualTo(newCategory.getType()).isNotEqualTo(oldCategory.getType());
        assertThat(categorySaved.getPattern()).isEqualTo(newCategory.getPattern()).isNotEqualTo(oldCategory.getPattern());
    }

    @Test
    void testFindByIdUnknownCategory() {
        final String categoryId = "377492af-03c2-46e3-b3d7-f824ebf08721";

        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.findById(UserStub.getUser(), categoryId));
    }

    @Test
    void testFindByIdWithDifferentUser() {
        final String categoryId = "377492af-03c2-46e3-b3d7-f824ebf08721";

        final User user = new User();
        user.setId("123");
        final Category category = new Category();
        category.setUser(user);

        when(repository.findById(any())).thenReturn(Optional.of(category));

        assertThrows(InvalidUserException.class, () -> service.findById(UserStub.getUser(), categoryId));
    }

    @Test
    void testFindById() throws CategoryNotFoundException, InvalidUserException {
        final String categoryId = "377492af-03c2-46e3-b3d7-f824ebf08721";
        final Category category = CategoryStub.getCategory();
        when(repository.findById(any())).thenReturn(Optional.of(category));

        final Category result = service.findById(UserStub.getUser(), categoryId);

        assertThat(result.getName()).isEqualTo(category.getName());
        assertThat(result.getType()).isEqualTo(category.getType());
        assertThat(result.getPattern()).isEqualTo(category.getPattern());
    }

    @Test
    void testDeleteUnknownCategory() {
        final String categoryId = "377492af-03c2-46e3-b3d7-f824ebf08721";
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.deleteById(UserStub.getUser(), categoryId));
        verify(repository, times(0)).deleteById(any());
    }

    @Test
    void testDeleteWithDifferentUser() {
        final String categoryId = "377492af-03c2-46e3-b3d7-f824ebf08721";

        final User user = new User();
        user.setId("123");
        final Category category = CategoryStub.getCategory();
        category.setUser(user);

        when(repository.findById(any())).thenReturn(Optional.of(category));

        assertThrows(InvalidUserException.class, () -> service.deleteById(UserStub.getUser(), categoryId));
        verify(repository, times(0)).deleteById(any());
    }

    @Test
    void testDelete() throws CategoryNotFoundException, InvalidUserException {
        final String categoryId = "377492af-03c2-46e3-b3d7-f824ebf08721";
        when(repository.findById(any())).thenReturn(Optional.of(CategoryStub.getCategory()));

        service.deleteById(UserStub.getUser(), categoryId);
        verify(repository).deleteById(eq(categoryId));
    }
}