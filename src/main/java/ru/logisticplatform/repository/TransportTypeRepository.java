package ru.logisticplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.order.TransportType;
import ru.logisticplatform.model.user.User;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link TransportType}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */



public interface TransportTypeRepository extends JpaRepository<TransportType, Long> {

}
