package br.com.andrebuarque.financeapi.service;

import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(final User user) {
        return userRepository.save(user);
    }
}
