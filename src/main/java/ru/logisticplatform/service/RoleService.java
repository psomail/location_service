package ru.logisticplatform.service;

import ru.logisticplatform.model.user.Role;

/**
 * Transportation interface for class {@link Role}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */


public interface RoleService {

    Role findByRoleName(String roleName);
}
