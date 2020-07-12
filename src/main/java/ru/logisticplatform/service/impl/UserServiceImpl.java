package ru.logisticplatform.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.Role;
import ru.logisticplatform.model.Status;
import ru.logisticplatform.model.User;
import ru.logisticplatform.repository.RoleRepository;
import ru.logisticplatform.repository.UserRepository;
import ru.logisticplatform.service.UserService;

import java.util.ArrayList;
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
    private final RoleRepository roleRepository;
    //private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository/*, BCryptPasswordEncoder passwordEncoder*/) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        //this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();

       // user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword("ddfdf");
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);

        log.info("IN UserServiceImpl register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        log.info("IN UserServiceImpl getAll");
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        log.info("IN UserServiceImpl findByUsername - user: {} found by username: {}", user, username);

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
    public void delete(Long id) {
        log.info("IN UserServiceImpl delete {}", id);
        userRepository.deleteById(id);
    }
}
