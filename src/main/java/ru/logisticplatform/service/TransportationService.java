package ru.logisticplatform.service;

import ru.logisticplatform.model.order.Transportation;
import ru.logisticplatform.model.user.User;

import java.util.List;

/**
 * Transportation interface for class {@link Transportation}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */


public interface TransportationService {

    Transportation create(Transportation transportation);

    List<Transportation> findByUser(User user);
}
