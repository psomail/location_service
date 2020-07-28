package ru.logisticplatform.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.order.TransportType;
import ru.logisticplatform.repository.TransportTypeRepository;
import ru.logisticplatform.service.TransportTypeService;

import java.util.List;


@Service
@Slf4j
public class TransportTypeServiceImpl implements TransportTypeService {

    private final TransportTypeRepository transportTypeRepository;

    @Autowired
    public TransportTypeServiceImpl(TransportTypeRepository transportTypeRepository){
        this.transportTypeRepository = transportTypeRepository;
    }

    @Override
    public TransportType findById(Long id) {
        TransportType transportType = transportTypeRepository.findById(id).orElse(null);

        if (transportType == null){
            log.warn("IN TransportTypeServiceImpl findById - no transportType found by id: {}", id);
            return null;
        }

        log.info("IN TransportTypeServiceImpl findById - transportType: {} found by id: {}", transportType);

        return transportType;
    }

    @Override
    public List<TransportType> getAll() {
        return null;
    }
}
