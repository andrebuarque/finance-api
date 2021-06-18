package br.com.andrebuarque.financeapi.repository;

import br.com.andrebuarque.financeapi.entity.Transaction;
import br.com.andrebuarque.financeapi.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Stream<Transaction> findByUser(User user);
}
