package ru.logisticplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.User;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link User}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
