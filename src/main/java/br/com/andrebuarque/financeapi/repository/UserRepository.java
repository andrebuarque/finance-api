package br.com.andrebuarque.financeapi.repository;

import br.com.andrebuarque.financeapi.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
