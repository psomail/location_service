package ru.logisticplatform.service;

import ru.logisticplatform.model.user.UserType;

/**
 * Transportation interface for class {@link UserType}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */


public interface UserTypeService {

    UserType findByUserTypeName(String userTypeName);

}
