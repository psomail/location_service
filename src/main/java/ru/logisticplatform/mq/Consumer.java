package ru.logisticplatform.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RabbitListener(queues = "create.user.queue")
        public void createUser(UserMq userMq){

        userMq.getRoles().forEach(role->role.setId(roleService.findByRoleName(role.getName()).getId()));

        this.userService.signUp(ObjectMapperUtils.map(userMq, User.class));

        log.info("!!!!!!!!!! create consume: " + userMq);
    }

        @RabbitListener(queues = "update.user.queue")
        public void updateUser(String msg){
            log.info("!!!!!!!!!! update consume: " + msg);
        }

    @RabbitListener(queues = "delete.user.queue")
    public void deleteUser(String msg){
        log.info("!!!!!!!!!! delete consume: " + msg);
    }
}