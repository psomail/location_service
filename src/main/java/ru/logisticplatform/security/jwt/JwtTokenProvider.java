package ru.logisticplatform.security.jwt;


import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Util class that provides methods for generation, validation, etc. of JWT token.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@Component
public class JwtTokenProvider {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

}
