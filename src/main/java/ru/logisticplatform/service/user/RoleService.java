package ru.logisticplatform.service.user;

import ru.logisticplatform.model.user.Role;

/**
 * Service interface for class {@link Role}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */


public interface RoleService {

    Role findByRoleName(String roleName);
}
