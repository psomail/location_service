package ru.logisticplatform.service.transportation.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationLocation;
import ru.logisticplatform.repository.transportation.TransportationLocationRepository;
import ru.logisticplatform.service.transportation.TransportationLocationService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TransportationLocationServiceImpl implements TransportationLocationService {

    private final TransportationLocationRepository transportationLocationRepository;

    @Autowired
    public TransportationLocationServiceImpl(TransportationLocationRepository transportationLocationRepository) {
        this.transportationLocationRepository = transportationLocationRepository;
    }


    @Override
    public List<TransportationLocation> findAll() {

        List<TransportationLocation> transportationLocations = transportationLocationRepository.findAll();

        if(transportationLocations.isEmpty()){
            log.warn("IN TransportationLocationServiceImpl findAll() - no transportationLocations found");
        }
        return transportationLocations;
    }

    @Override
    public List<TransportationLocation> findAllByCoordinates(Double lonFrom, Double lonTo, Double latFrom, Double latTo) {

        List<TransportationLocation> transportationLocations = transportationLocationRepository
                                                                    .findAllByLonBetweenAndLatBetween(lonFrom
                                                                                                        ,lonTo
                                                                                                        ,latFrom
                                                                                                        ,latTo);


        return null;
    }

    @Override
    public TransportationLocation findByTransportation(Transportation transportation) {

        TransportationLocation transportationLocation = transportationLocationRepository.findByTransportation(transportation);

        if(transportationLocation == null){
            log.warn("IN TransportationLocationServiceImpl findByTransportation - no transportationLocation" +
                    " found by transportation id: {}", transportation.getId());
            return null;
        }

        log.info("IN TransportationLocationServiceImpl findById - transportation: {} by transportation id: {}", transportation.getId());

        return transportationLocation;
    }

    @Override
    public List<TransportationLocation> findAllByTransportTypes(Double lonFrom, Double lonTo, Double latFrom, Double latTo, List<Long> ids) {

        List<TransportationLocation> transportationLocations
                = transportationLocationRepository.findAllByLonBetweenAndLatBetweenAndTransportation_TransportType_IdIn(lonFrom
                                                                                                                        ,lonTo
                                                                                                                        ,latFrom
                                                                                                                        ,latTo
                                                                                                                        ,ids);
        if(transportationLocations.isEmpty()){
            log.warn("IN TransportationLocationServiceImpl findAllByLonBetweenAndLatBetween() - no transportationLocations found");
        }

        return transportationLocations;
    }

    @Override
    public TransportationLocation save(TransportationLocation transportationLocation) {

        log.info("IN TransportationLocationServiceImpl create() - transportationLocation" +
                " for transportation id: {} successfully created", transportationLocation.getTransportation().getId());

        return transportationLocationRepository.save(transportationLocation);
    }

    @Scheduled(initialDelay = 10000, fixedDelayString = "${schedule.delete.transportation.location}")
    @Transactional
    public void deleteSchedule() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.MINUTE, -20);
        Date previousMinute = calendar.getTime();
       // transportationLocationRepository.deleteAllByUpdatedBefore(previousMinute);
        log.info("IN TransportationLocationServiceImpl deleteSchedule() - deleted offline transportation");
    }
}
