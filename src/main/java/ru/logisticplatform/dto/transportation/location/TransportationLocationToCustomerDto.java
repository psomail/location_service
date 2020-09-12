package ru.logisticplatform.dto.transportation.location;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.dto.transportation.TransportationForCustomerDto;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportationLocationToCustomerDto {

    TransportationForCustomerDto transportation;
    Double lat;
    Double lon;
}
