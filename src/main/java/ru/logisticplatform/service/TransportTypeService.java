package ru.logisticplatform.service;

import ru.logisticplatform.model.order.TransportType;

import java.util.List;

public interface TransportTypeService {

    TransportType findById(Long id);

    List<TransportType> getAll();
}
