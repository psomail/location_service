package ru.logisticplatform.service.user;

import ru.logisticplatform.model.user.Role;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.mq.UserMq;

import java.util.List;

/**
 * Service interface for class {@link User}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface UserService{

    User signUp(User user);

    User updateUser(UserMq userMq);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    User updateUserStatus(User user, UserStatus userStatus);

    void delete(Long id);
    void delete(String userName);
    void delete(User user);

    List<User> findByStatus(UserStatus userStatus);
}

