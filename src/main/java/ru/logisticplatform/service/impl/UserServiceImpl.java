package ru.logisticplatform.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.Status;
import ru.logisticplatform.model.User;
import ru.logisticplatform.repository.UserRepository;
import ru.logisticplatform.service.UserService;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserService} interface.
 * Wrapper for {@link UserRepository} + business logic.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User signUp(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User signUpUser = userRepository.save(user);

        log.info("IN UserServiceImpl signUp - user: {} successfully registered", signUpUser);

        return signUpUser;
    }

    @Override
    public List<User> getAll() {
        log.info("IN UserServiceImpl getAll");
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        log.info("IN UserServiceImpl findByUsername - user: {} found by userName: {}", user, username);
        return user;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null){
            log.warn("IN UserServiceImpl findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN UserServiceImpl findById - user: {} found by id: {}", result);
        return result;
    }

    @Override
    public User updateUserStatus(User user, Status status) {

        user.setStatus(status);

        User updatedUser = userRepository.save(user);

        log.info("IN UserServiceImpl setUserStatusToDeleted - user: {} successfully set status: {}", updatedUser, status);

        return updatedUser;
    }

    @Override
    public void delete(Long id) {
        log.info("IN UserServiceImpl delete {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findByStatus(Status status) {
        return userRepository.findByStatus(status);
    }
}
