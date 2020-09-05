package ru.logisticplatform.repository.transportation;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.transportation.TransportType;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationStatus;
import ru.logisticplatform.model.user.User;

import java.util.List;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link Transportation}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface TransportationRepository extends JpaRepository<Transportation, Long> {

    List<Transportation> findAllByUser(User user);

    Transportation findByTransportTypeAndModelAndUserAndTransportationStatusNotLike(TransportType transportType
                                                                                    ,String model
                                                                                    ,User user
                                                                                    ,TransportationStatus transportationStatusNotLike);

    List<Transportation> findAllByUserAndTransportationStatus(User user, TransportationStatus transportationStatus);

    List<Transportation> findAllByUserAndTransportationStatusNotLike(User user, TransportationStatus transportationStatusNotLike);

}
