package ru.logisticplatform.service;

import ru.logisticplatform.model.order.Service;
import ru.logisticplatform.model.user.User;

import java.util.List;

/**
 * Service interface for class {@link Service}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */


public interface ServiceService {

    Service create(Service service);

    List<Service> findByUser(User user);
}
