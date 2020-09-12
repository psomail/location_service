package ru.logisticplatform.dto.transportation;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.dto.user.UserDto;
import ru.logisticplatform.model.transportation.TransportationStatus;
import ru.logisticplatform.model.user.User;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportationForCustomerDto {
    Long id;
    UserDto user;
    TransportTypeDto transportType;
    String model;
}
