package ru.logisticplatform.service.transportation;


import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationLocation;

public interface TransportationLocationService {

    TransportationLocation findByTransportation(Transportation transportation);

    TransportationLocation save(TransportationLocation transportationLocation);
    //TransportationLocation update(TransportationLocation transportationLocation);

}
