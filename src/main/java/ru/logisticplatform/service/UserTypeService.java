package ru.logisticplatform.service;

import ru.logisticplatform.model.user.UserType;

/**
 * Service interface for class {@link UserType}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */


public interface UserTypeService {

    UserType findByUserTypeName(String userTypeName);
}
