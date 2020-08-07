package ru.logisticplatform.service.transportation;

import ru.logisticplatform.model.transportation.TransportType;

import java.util.List;

public interface TransportTypeService {

    TransportType findById(Long id);

    List<TransportType> getAll();
}
