package ru.logisticplatform.service.transportation.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.transportation.TransportType;
import ru.logisticplatform.repository.transportation.TransportTypeRepository;
import ru.logisticplatform.service.transportation.TransportTypeService;

import java.util.List;


@Service
@Slf4j
public class TransportTypeServiceImpl implements TransportTypeService {

    private final TransportTypeRepository transportTypeRepository;

    @Autowired
    public TransportTypeServiceImpl(TransportTypeRepository transportTypeRepository) {
        this.transportTypeRepository = transportTypeRepository;
    }

    @Override
    public TransportType findById(Long id) {
        TransportType transportType = transportTypeRepository.findById(id).orElse(null);

        if (transportType == null){
            log.warn("IN TransportTypeServiceImpl findById - no transportType found by id: {}", id);
            return null;
        }

        log.info("IN TransportTypeServiceImpl findById - transportType: {} found by id: {}", transportType, id);

        return transportType;
    }

    @Override
    public List<TransportType> getAll() {
        return null;
    }
}
