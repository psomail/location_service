package ru.logisticplatform.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.user.Role;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.repository.user.RoleRepository;
import ru.logisticplatform.service.user.RoleService;

/**
 * Implementation of {@link RoleService} interface.
 * Wrapper for {@link RoleRepository} + business logic.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
              this.roleRepository = roleRepository;
    }

    @Override
    public Role findByRoleName(String roleName) {
        Role role = roleRepository.findByName(roleName);
        log.info("IN RoleServiceImpl findByRoleName - role: {} found by roleName: {}", role, roleName);
        return role;
    }

    @Override
    public Boolean findUserRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(s->s.getName().contains(roleName));
    }
}
