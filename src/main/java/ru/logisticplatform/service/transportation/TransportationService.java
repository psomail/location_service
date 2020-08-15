package ru.logisticplatform.service.transportation;

import ru.logisticplatform.model.transportation.Transportation;

public interface TransportationService {

    Transportation findById(Long id);

    Transportation createTransportation(Transportation transportation);

    Transportation updateTransportation(Transportation transportation);

    void delete(Long id);
}
