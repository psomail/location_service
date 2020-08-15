package ru.logisticplatform.dto.user;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO class for authentication (login) request.
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class AuthenticationRequestDto {
    String username;
    String password;
}
