package ru.logisticplatform.service;

import ru.logisticplatform.model.user.Status;
import ru.logisticplatform.model.user.User;
import java.util.List;

/**
 * Service interface for class {@link User}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface UserService{

    User signUp(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    User updateUserStatus(User user, Status status);

    void delete(Long id);

    List<User> findByStatus(Status status);

}

