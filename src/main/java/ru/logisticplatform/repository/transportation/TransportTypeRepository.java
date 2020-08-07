package ru.logisticplatform.repository.transportation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.transportation.TransportType;
/**
 * Repository interface that extends {@link JpaRepository} for class {@link TransportType}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface TransportTypeRepository extends JpaRepository<TransportType, Long> {

}
