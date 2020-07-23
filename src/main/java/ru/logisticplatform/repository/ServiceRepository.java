package ru.logisticplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.order.Service;
import ru.logisticplatform.model.user.User;

import java.util.List;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link Service}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findByUser(User user);
}
