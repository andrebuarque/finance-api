package br.com.andrebuarque.financeapi.repository;

import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Stream<Category> findByUser(User user);
}
