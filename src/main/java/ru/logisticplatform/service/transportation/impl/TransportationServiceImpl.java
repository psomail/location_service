package ru.logisticplatform.service.transportation.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.repository.transportation.TransportationRepository;
import ru.logisticplatform.service.transportation.TransportationService;

@Service
@Slf4j
public class TransportationServiceImpl implements TransportationService {

    private final TransportationRepository transportationRepository;

    @Autowired
    public TransportationServiceImpl(TransportationRepository transportationRepository) {
        this.transportationRepository = transportationRepository;
    }

    /**
     *
     * @param id
     * @return
     */

    @Override
    public Transportation findById(Long id) {

        Transportation transportation = this.transportationRepository.findById(id).orElse(null);

        if (transportation == null){
            log.warn("IN TransportationServiceImpl findById - no transportation found by id: {}", id);
            return null;
        }

        log.info("IN TransportationServiceImpl findById - transportation: {} found by id: {}", transportation, id);

        return transportation;
    }

    @Override
    public Transportation createTransportation(Transportation transportation) {
        return null;
    }

    @Override
    public Transportation updateTransportation(Transportation transportation) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
