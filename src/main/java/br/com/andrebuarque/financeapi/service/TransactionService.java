package br.com.andrebuarque.financeapi.service;

import br.com.andrebuarque.financeapi.dto.TransactionDto;
import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.Transaction;
import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.exception.CategoryNotFoundException;
import br.com.andrebuarque.financeapi.exception.InvalidUserException;
import br.com.andrebuarque.financeapi.exception.TransactionNotFoundException;
import br.com.andrebuarque.financeapi.mapper.TransactionMapper;
import br.com.andrebuarque.financeapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

import static br.com.andrebuarque.financeapi.validation.BeanValidator.validate;

@Service
public class TransactionService {
    private static final TransactionMapper MAPPER = TransactionMapper.INSTANCE;
    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;

    public TransactionService(final TransactionRepository transactionRepository, final CategoryService categoryService) {
        this.transactionRepository = transactionRepository;
        this.categoryService = categoryService;
    }

    public Stream<Transaction> findAll(final User user) {
        return transactionRepository.findByUser(user);
    }

    public Transaction create(final User user, final TransactionDto transactionDto) throws CategoryNotFoundException, InvalidUserException {
        validate(transactionDto);

        final Transaction transaction = toEntity(user, transactionDto);

        return transactionRepository.save(transaction);
    }

    public Transaction update(final User user, final String transactionId, final TransactionDto transactionDto) throws InvalidUserException, TransactionNotFoundException, CategoryNotFoundException {
        assert Objects.nonNull(transactionId);
        validate(transactionDto);

        Transaction transaction = findById(transactionId);
        validateSameUserFromTransaction(user, transaction);

        final Transaction newTransaction = toEntity(user, transactionDto);
        newTransaction.setId(transactionId);

        return transactionRepository.save(newTransaction);
    }

    public Transaction findById(final User user, final String transactionId) throws InvalidUserException, TransactionNotFoundException {
        final Transaction transaction = findById(transactionId);
        validateSameUserFromTransaction(user, transaction);
        return transaction;
    }

    public void deleteById(final User user, final String transactionId) throws InvalidUserException, TransactionNotFoundException {
        findById(user, transactionId);
        transactionRepository.deleteById(transactionId);
    }

    private Transaction toEntity(final User user, final TransactionDto transactionDto) throws CategoryNotFoundException, InvalidUserException {
        final String categoryId = transactionDto.getCategoryId();
        final Transaction transaction = MAPPER.toEntity(transactionDto);

        transaction.setUser(user);
        if (Objects.nonNull(categoryId)) {
            transaction.setCategory(findCategoryById(user, categoryId));
        }

        return transaction;
    }

    private Category findCategoryById(final User user, final String categoryId) throws CategoryNotFoundException, InvalidUserException {
        return categoryService.findById(user, categoryId);
    }

    private void validateSameUserFromTransaction(final User user, final Transaction transaction) throws InvalidUserException {
        if (!user.getId().equals(transaction.getUser().getId())) {
            throw new InvalidUserException();
        }
    }

    private Transaction findById(final String transactionId) throws TransactionNotFoundException {
        return transactionRepository
            .findById(transactionId)
            .orElseThrow(TransactionNotFoundException::new);
    }
}
