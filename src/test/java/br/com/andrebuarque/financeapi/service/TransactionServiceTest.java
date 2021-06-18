package br.com.andrebuarque.financeapi.service;

import br.com.andrebuarque.financeapi.dto.TransactionDto;
import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.Transaction;
import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.exception.CategoryNotFoundException;
import br.com.andrebuarque.financeapi.exception.InvalidUserException;
import br.com.andrebuarque.financeapi.exception.TransactionNotFoundException;
import br.com.andrebuarque.financeapi.repository.TransactionRepository;
import br.com.andrebuarque.financeapi.stub.CategoryStub;
import br.com.andrebuarque.financeapi.stub.TransactionDtoStub;
import br.com.andrebuarque.financeapi.stub.TransactionStub;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @InjectMocks
    TransactionService service;

    @Mock
    TransactionRepository repository;

    @Mock
    CategoryService categoryService;

    @Captor
    ArgumentCaptor<Transaction> transactionArgumentCaptor;

    @Test
    void testFindAll() {
        final Transaction transaction = TransactionStub.getTransaction();
        when(repository.findByUser(any())).thenReturn(Stream.of(transaction));

        final Stream<Transaction> result = service.findAll(new User());

        final Optional<Transaction> first = result.findFirst();
        assertThat(first).contains(transaction);
    }

    @Test
    void testCreateWithInvalidTransaction() {
        assertThrows(ConstraintViolationException.class,
            () -> service.create(UserStub.getUser(), new TransactionDto()));

        verify(repository, times(0)).save(any());
    }

    @Test
    void testCreateWithDifferentUserOfCategory() throws CategoryNotFoundException, InvalidUserException {
        doThrow(new InvalidUserException()).when(categoryService).findById(any(), any());

        assertThrows(InvalidUserException.class,
            () -> service.create(UserStub.getUser(), TransactionDtoStub.getTransactionDto()));

        verify(repository, times(0)).save(any());
    }

    @Test
    void testCreateWithUnknownCategory() throws CategoryNotFoundException, InvalidUserException {
        doThrow(new CategoryNotFoundException()).when(categoryService).findById(any(), any());

        assertThrows(CategoryNotFoundException.class,
            () -> service.create(UserStub.getUser(), TransactionDtoStub.getTransactionDto()));

        verify(repository, times(0)).save(any());
    }

    @Test
    void testCreate() throws CategoryNotFoundException, InvalidUserException {
        final TransactionDto transactionDto = TransactionDtoStub.getTransactionDto();
        final Category category = CategoryStub.getCategory();
        final User user = UserStub.getUser();

        when(categoryService.findById(any(), any())).thenReturn(category);

        service.create(user, transactionDto);

        verify(repository).save(transactionArgumentCaptor.capture());
        final Transaction captorValue = transactionArgumentCaptor.getValue();

        assertThat(captorValue.getUser()).isEqualTo(user);
        assertThat(captorValue.getCategory()).isEqualTo(category);
        assertThat(captorValue.getDescription()).isEqualTo(transactionDto.getDescription());
        assertThat(captorValue.getType()).isEqualTo(transactionDto.getType());
        assertThat(captorValue.getValue()).isEqualTo(transactionDto.getValue());
        assertThat(captorValue.getDate()).isEqualTo(transactionDto.getDate());
        assertThat(captorValue.getStatus()).isEqualTo(transactionDto.getStatus());
    }

    @Test
    void testUpdateWithoutTransactionId() {
        assertThrows(AssertionError.class, () ->
            service.update(UserStub.getUser(), null, TransactionDtoStub.getTransactionDto()));

        verify(repository, times(0)).save(any());
    }

    @Test
    void testUpdateWithInvalidTransaction() {
        assertThrows(ConstraintViolationException.class, () ->
            service.update(UserStub.getUser(), "id", new TransactionDto()));

        verify(repository, times(0)).save(any());
    }

    @Test
    void testUpdateWithUnknownTransaction() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () ->
            service.update(UserStub.getUser(), "id", TransactionDtoStub.getTransactionDto()));

        verify(repository, times(0)).save(any());
    }

    @Test
    void testUpdateWithDifferentUserFromTransaction() {
        final Transaction transaction = new Transaction();
        transaction.setUser(User.builder().id("id").build());

        when(repository.findById(any())).thenReturn(Optional.of(transaction));

        assertThrows(InvalidUserException.class, () ->
            service.update(UserStub.getUser(), "id", TransactionDtoStub.getTransactionDto()));

        verify(repository, times(0)).save(any());
    }

    @Test
    void testUpdateWithUnknownCategory() throws CategoryNotFoundException, InvalidUserException {
        when(repository.findById(any())).thenReturn(Optional.of(TransactionStub.getTransaction()));
        doThrow(new CategoryNotFoundException()).when(categoryService).findById(any(), any());

        assertThrows(CategoryNotFoundException.class, () ->
            service.update(UserStub.getUser(), "id", TransactionDtoStub.getTransactionDto()));

        verify(repository, times(0)).save(any());
    }

    @Test
    void testUpdate() throws CategoryNotFoundException, InvalidUserException, TransactionNotFoundException {
        final Category category = CategoryStub.getCategory();
        final Transaction transaction = TransactionStub.getTransaction();
        final TransactionDto transactionDto = TransactionDtoStub.getTransactionDto();
        final User user = UserStub.getUser();
        final String transactionId = "id";

        when(repository.findById(any())).thenReturn(Optional.of(transaction));
        when(categoryService.findById(any(), any())).thenReturn(category);

        service.update(user, transactionId, transactionDto);

        verify(repository).save(transactionArgumentCaptor.capture());
        final Transaction captorValue = transactionArgumentCaptor.getValue();

        assertThat(captorValue.getId()).isEqualTo(transactionId);
        assertThat(captorValue.getUser()).isEqualTo(user);
        assertThat(captorValue.getCategory()).isEqualTo(category);
        assertThat(captorValue.getDescription()).isNotEqualTo(transaction.getDescription()).isEqualTo(transactionDto.getDescription());
        assertThat(captorValue.getType()).isNotEqualTo(transaction.getType()).isEqualTo(transactionDto.getType());
        assertThat(captorValue.getValue()).isNotEqualTo(transaction.getValue()).isEqualTo(transactionDto.getValue());
        assertThat(captorValue.getDate()).isNotEqualTo(transaction.getDate()).isEqualTo(transactionDto.getDate());
        assertThat(captorValue.getStatus()).isNotEqualTo(transaction.getStatus()).isEqualTo(transactionDto.getStatus());
    }

    @Test
    void testFindByIdWithUnknownTransactionId() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () ->
            service.findById(UserStub.getUser(), "id"));
    }

    @Test
    void testFindByIdWithDifferentUser() {
        final Transaction transaction = new Transaction();
        transaction.setUser(User.builder().id("id").build());

        when(repository.findById(any())).thenReturn(Optional.of(transaction));

        assertThrows(InvalidUserException.class, () ->
            service.findById(UserStub.getUser(), "id"));
    }

    @Test
    void testFindById() throws TransactionNotFoundException, InvalidUserException {
        final Transaction transaction = TransactionStub.getTransaction();

        when(repository.findById(any())).thenReturn(Optional.of(transaction));

        final Transaction result = service.findById(UserStub.getUser(), "id");

        assertThat(result).isEqualTo(transaction);
    }

    @Test
    void testDeleteByIdWithUnknownTransactionId() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () ->
            service.deleteById(UserStub.getUser(), "id"));

        verify(repository, times(0)).deleteById(any());
    }

    @Test
    void testDeleteByIdWithDifferentUser() {
        final Transaction transaction = new Transaction();
        transaction.setUser(User.builder().id("id").build());

        when(repository.findById(any())).thenReturn(Optional.of(transaction));

        assertThrows(InvalidUserException.class, () ->
            service.deleteById(UserStub.getUser(), "id"));

        verify(repository, times(0)).deleteById(any());
    }

    @Test
    void testDeleteById() throws TransactionNotFoundException, InvalidUserException {
        final String transactionId = "id";

        when(repository.findById(any())).thenReturn(Optional.of(TransactionStub.getTransaction()));

        service.deleteById(UserStub.getUser(), transactionId);

        verify(repository).deleteById(eq(transactionId));
    }
}