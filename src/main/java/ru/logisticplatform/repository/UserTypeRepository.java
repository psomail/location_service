package ru.logisticplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.UserType;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link UserType}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    UserType findByName(String name);
}
