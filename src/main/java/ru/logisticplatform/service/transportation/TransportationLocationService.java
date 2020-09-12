package ru.logisticplatform.service.transportation;


import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationLocation;

import java.util.List;

public interface TransportationLocationService {

    List<TransportationLocation> findAll();

    TransportationLocation findByTransportation(Transportation transportation);

    List<TransportationLocation> findAllByCoordinates(Double lonFrom, Double lonTo, Double latFrom, Double latTo);

    List<TransportationLocation> findAllByTransportTypes(Double lonFrom, Double lonTo, Double latFrom, Double latTo, List<Long> ids);

    TransportationLocation save(TransportationLocation transportationLocation);

}
