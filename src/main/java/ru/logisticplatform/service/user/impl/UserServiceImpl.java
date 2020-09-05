package ru.logisticplatform.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.mq.UserMq;
import ru.logisticplatform.repository.user.UserRepository;
import ru.logisticplatform.service.user.UserService;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

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

    /**
     *
     * @param user
     * @return
     */

    @Override
    public User signUp(User user) {

        User signUpUser = userRepository.save(user);

        log.info("IN UserServiceImpl signUp - user: {} successfully registered", signUpUser.getUsername());

        return signUpUser;
    }

    @Override
    public User updateUser(UserMq userMq) {

        User updateUser = this.userRepository.findByUsername(userMq.getUsername());
        updateUser.setFirstName(userMq.getFirstName());
        updateUser.setLastName(userMq.getLastName());
        updateUser.setEmail(userMq.getEmail());
        updateUser.setPhone(userMq.getPhone());

        User updatedUser = userRepository.save(updateUser);

        log.info("IN UserServiceImpl updateUser - user: {} successfully updated", updatedUser.getUsername());

        return updatedUser;
    }

    @Override
    public List<User> getAll() {

        List<User> users = userRepository.findAll();

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
        User user = userRepository.findById(id).orElse(null);

        if (user == null){
            log.warn("IN UserServiceImpl findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN UserServiceImpl findById - user: {} found by id: {}", user.getUsername(), id);
        return user;
    }

    @Override
    public User updateUserStatus(User user, UserStatus userStatus) {

        user.setUserStatus(userStatus);

        User updatedUser = userRepository.save(user);

        log.info("IN UserServiceImpl updateUserStatus - user: {} successfully set userStatus: {}", updatedUser, userStatus);

        return updatedUser;
    }

    @Override
    public void delete(Long id) {
        log.info("IN UserServiceImpl delete {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public void delete(String userName) {

        User userDelete = userRepository.findByUsername(userName);
        userRepository.delete(userDelete);
        log.info("IN UserServiceImpl delete {}", userName);
    }

    @Override
    public void delete(User user) {
        log.info("IN UserServiceImpl delete {}", user.getUsername());
        userRepository.delete(user);
    }

    @Override
    public List<User> findByStatus(UserStatus userStatus) {
        return userRepository.findByUserStatus(userStatus);
    }

}
