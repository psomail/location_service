package ru.logisticplatform.repository.transportation;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.user.User;

import java.util.List;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link Transportation}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface TransportationRepository extends JpaRepository<Transportation, Long> {

    //   List<Transportation> findByUser(User user);
}
