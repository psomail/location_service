package ru.logisticplatform.service.transportation.impl;

import liquibase.pro.packaged.T;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.transportation.TransportType;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.repository.transportation.TransportationRepository;
import ru.logisticplatform.service.transportation.TransportationService;

import java.util.List;

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

    /**
     *
     * @return
     */

    @Override
    public List<Transportation> findAll() {

        List<Transportation> transportations = this.transportationRepository.findAll();

        if (transportations.isEmpty()){
            log.warn("IN TransportationServiceImpl findAllByUser - no transportations found ");
        }

        return transportations;
    }

    @Override
    public List<Transportation> findAllByTransportationStatus(TransportationStatus status) {

        List<Transportation> transportations = this.transportationRepository.findAllByTransportationStatus(status);

        if (transportations.isEmpty()){
            log.warn("IN TransportationServiceImpl findAllByTransportationStatus - no transportations found by status: {}", status.toString());
        }

        return transportations;
    }

    /**
     *
     * @param user
     * @return
     */

    @Override
    public List<Transportation> findAllByUser(User user) {

        List<Transportation> transportations = this.transportationRepository.findAllByUser(user);

        if (transportations.isEmpty()){
            log.warn("IN TransportationServiceImpl findAllByUser - no transportations found by user: {}", user.getUsername());
        }

        return transportations;
    }

    @Override
    public List<Transportation> findAllByUserAndStatus(User user, TransportationStatus status) {

        List<Transportation> transportations = this.transportationRepository.findAllByUserAndTransportationStatus(user, status);

        if (transportations.isEmpty()){
            log.warn("IN TransportationServiceImpl findAllByUserAndStatus - no transportations found by user: {} " +
                    " and transportation status: {}", user.getUsername(), status.toString());
        }

        return transportations;
    }

    @Override
    public List<Transportation> findAllByUserAndStatusNotLike(User user, TransportationStatus status) {

        List<Transportation> transportations = this.transportationRepository.findAllByUserAndTransportationStatusNotLike(user, status);

        if (transportations.isEmpty()){
            log.warn("IN TransportationServiceImpl findAllByUserAndStatusNotLike() - no transportations found by user: {} " +
                    " and transportation status not like: {}", user.getUsername(), status.toString());
        }

        return transportations;
    }

    /**
     *
     * @param transportType
     * @param model
     * @param user
     * @return
     */
    @Override
    public Transportation findByTransportTypeAndModelAndUserAndTransportationStatusNotLike(TransportType transportType
                                                                                        ,String model
                                                                                        ,User user
                                                                                        ,TransportationStatus transportationStatus){
        Transportation transportation = this.transportationRepository
                                            .findByTransportTypeAndModelAndUserAndTransportationStatusNotLike(transportType
                                                                                                                ,model
                                                                                                                ,user
                                                                                                                ,transportationStatus);
        if(transportation == null) {
            log.warn("IN TransportationServiceImpl findByTransportTypeAndModelAndUser() - no transportations found by transportType: {} " +
                    ", model: {} , user: {} ", transportType.getName(), model, user.getUsername());
        }
        return transportation;
    }

    @Override
    public Transportation createTransportation(Transportation transportation) {

        transportationRepository.save(transportation);

        log.info("IN TransportationServiceImpl createTransportation() - transportation ID: {} successfully created", transportation.getId());

        return transportation;
    }

    @Override
    public Transportation updateTransportation(Transportation transportation) {

        this.transportationRepository.save(transportation);
        log.info("IN TransportationServiceImpl updateTransportation() - transportation ID: {} successfully updated", transportation.getId());

        return transportation;
    }

    @Override
    public void delete(Long id) {

    }
}
