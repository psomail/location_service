package ru.logisticplatform.repository.transportation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.transportation.TransportType;

import java.util.List;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link TransportType}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface TransportTypeRepository extends JpaRepository<TransportType, Long> {

    List<TransportType> findAllByIdIn(List<Long> ids);

}
