package ru.logisticplatform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.logisticplatform.model.order.Service;

import ru.logisticplatform.model.user.User;
import ru.logisticplatform.repository.ServiceRepository;

import java.util.List;


@org.springframework.stereotype.Service
@Slf4j
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Service create(Service service) {
        return null;
    }

    @Override
    public List<Service> findByUser(User user) {
        return null;
    }
}
