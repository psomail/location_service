package ru.logisticplatform.repository.transportation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationLocation;

import java.util.Date;
import java.util.List;

public interface TransportationLocationRepository extends JpaRepository<TransportationLocation, Long> {

    TransportationLocation findByTransportation(Transportation transportation);

    List<TransportationLocation> findAllByLonBetweenAndLatBetween(Double lonFrom, Double lonTo, Double latFrom, Double latTo);

    List<TransportationLocation> findAllByLonBetweenAndLatBetweenAndTransportation_TransportType_IdIn(Double lonFrom, Double lonTo, Double latFrom, Double latTo, List<Long> ids);

    void deleteAllByUpdatedBefore(Date updatedBefore);
}
