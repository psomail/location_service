package ru.logisticplatform.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.logisticplatform.model.Status;


//@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUserDto extends UserDto{
    String password;
    Status status;
}
