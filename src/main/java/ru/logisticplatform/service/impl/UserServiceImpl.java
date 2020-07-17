package ru.logisticplatform.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.Role;
import ru.logisticplatform.model.Status;
import ru.logisticplatform.model.User;
import ru.logisticplatform.model.UserType;
import ru.logisticplatform.repository.RoleRepository;
import ru.logisticplatform.repository.UserRepository;
import ru.logisticplatform.repository.UserTypeRepository;
import ru.logisticplatform.service.UserService;

import java.util.ArrayList;
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
    private final RoleRepository roleRepository;
    private final UserTypeRepository userTypeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, UserTypeRepository userTypeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public User signup(User user) {
//        Role roleUser1 = roleRepository.findByName("ROLE_USER");
//        Role roleUser2 = roleRepository.findByName("ROLE_ADMIN");
//        List<Role> userRoles = new ArrayList<>();
//        userRoles.add(roleUser1);
//        userRoles.add(roleUser2);



//        UserType userType = userTypeRepository.findByName("USERTYPE_CONTRACTOR");
//        List<UserType> userTypes = new ArrayList<>();
//        userTypes.add(userType);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setStatus(Status.ACTIVE);
//        Status st = user.getStatus();
//        System.out.println(st);
//
//        user.setUserTypes(userTypes);

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
    public void delete(Long id) {
        log.info("IN UserServiceImpl delete {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findByStatus(Status status) {
        return userRepository.findByStatus(status);
    }
}
