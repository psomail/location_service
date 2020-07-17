package ru.logisticplatform.service;

import ru.logisticplatform.model.Status;
import ru.logisticplatform.model.User;
import java.util.List;

/**
 * Service interface for class {@link User}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface UserService{

    User signup(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);

    List<User> findByStatus(Status status);

}

