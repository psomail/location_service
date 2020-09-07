package ru.logisticplatform.repository.transportation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationLocation;

import java.util.Date;

public interface TransportationLocationRepository extends JpaRepository<TransportationLocation, Long> {

    TransportationLocation findByTransportation(Transportation transportation);

    void deleteAllByUpdatedBefore(Date updatedBefore);
}
