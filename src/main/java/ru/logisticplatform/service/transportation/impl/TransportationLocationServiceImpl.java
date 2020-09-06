package ru.logisticplatform.service.transportation.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationLocation;
import ru.logisticplatform.repository.transportation.TransportationLocationRepository;
import ru.logisticplatform.service.transportation.TransportationLocationService;

@Service
@Slf4j
public class TransportationLocationServiceImpl implements TransportationLocationService {

    private final TransportationLocationRepository transportationLocationRepository;

    @Autowired
    public TransportationLocationServiceImpl(TransportationLocationRepository transportationLocationRepository) {
        this.transportationLocationRepository = transportationLocationRepository;
    }

    @Override
    public TransportationLocation findByTransportation(Transportation transportation) {

        TransportationLocation transportationLocation = transportationLocationRepository.findByTransportation(transportation);

        if(transportationLocation == null){
            log.warn("IN TransportationLocationServiceImpl findByTransportation - no transportationLocation" +
                    " found by transportation id: {}", transportation.getId());
            return null;
        }

        log.info("IN TransportationServiceImpl findById - transportation: {} by transportation id: {}", transportation.getId());

        return transportationLocation;
    }

    @Override
    public TransportationLocation save(TransportationLocation transportationLocation) {

        log.info("IN TransportationLocationServiceImpl create() - transportationLocation" +
                " for transportation id: {} successfully created", transportationLocation.getTransportation().getId());

        return transportationLocationRepository.save(transportationLocation);
    }
//
//    @Override
//    public TransportationLocation update(TransportationLocation transportationLocation) {
//        return null;
//    }
}
