package ru.logisticplatform.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.service.user.RoleService;
import ru.logisticplatform.service.user.UserService;

@Component
@Slf4j
public class Consumer {

    private final UserService userService;
    private final RoleService roleService;

   @Autowired
    public Consumer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RabbitListener(queues = "${create.user.queue}")
        public void createUser(UserMq userMq){

        User user = userService.findByUsername(userMq.getUsername());

        if(user == null){
            userMq.getRoles().forEach(role->role.setId(roleService.findByRoleName(role.getName()).getId()));

            User newUser = ObjectMapperUtils.map(userMq, User.class);
            this.userService.signUp(newUser);

            log.info("IN Consumer createUser() - user: {} successfully registered", userMq.getUsername());
        }

    }

    @RabbitListener(queues = "${update.user.queue}")
    public void updateUser(UserMq userMq){

        userMq.getRoles().forEach(role->role.setId(roleService.findByRoleName(role.getName()).getId()));
        this.userService.updateUser(userMq);

        log.info("IN Consumer updateUser() - user: {} successfully updated", userMq.getUsername());
    }

    @RabbitListener(queues = "${delete.user.queue}")
    public void deleteUser(UserMq userMq){

       User userDelete = this.userService.findByUsername(userMq.getUsername());
       this.userService.delete(userDelete);
       log.info("IN Consumer deleteUser() user: {} successfully deleted", userMq.getUsername());
    }
}