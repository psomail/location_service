package ru.logisticplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.Role;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link Role}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
