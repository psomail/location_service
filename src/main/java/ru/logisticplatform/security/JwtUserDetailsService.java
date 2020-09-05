package ru.logisticplatform.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.mq.ProducerRabbitMq;
import ru.logisticplatform.security.jwt.JwtUser;
import ru.logisticplatform.security.jwt.JwtUserFactory;
import ru.logisticplatform.service.user.UserService;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final ProducerRabbitMq producerRabbitMq;

    @Autowired
    public JwtUserDetailsService(UserService userService
                                ,ProducerRabbitMq producerRabbitMq) {
        this.userService = userService;
        this.producerRabbitMq = producerRabbitMq;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null) {
            producerRabbitMq.unknownUserProduce(username);

            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);
        return jwtUser;
    }


}
