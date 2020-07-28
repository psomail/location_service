package ru.logisticplatform.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.logisticplatform.model.order.Transportation;

import ru.logisticplatform.model.user.User;
import ru.logisticplatform.repository.TransportationRepository;
import ru.logisticplatform.service.TransportationService;

import java.util.List;


@org.springframework.stereotype.Service
@Slf4j
public class TransportationServiceImpl implements TransportationService {

    private final TransportationRepository transportationRepository;

    @Autowired
    public TransportationServiceImpl(TransportationRepository transportationRepository) {
        this.transportationRepository = transportationRepository;
    }

    @Override
    public Transportation create(Transportation transportation) {
        return null;
    }

    @Override
    public List<Transportation> findByUser(User user) {
        return null;
    }
}
