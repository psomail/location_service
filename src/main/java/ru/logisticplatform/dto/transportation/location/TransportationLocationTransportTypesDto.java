package ru.logisticplatform.dto.transportation.location;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.dto.transportation.TransportTypeCreateTransportationDto;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportationLocationTransportTypesDto extends TransportationLocationСoordinatesDto {

    List<TransportTypeCreateTransportationDto> transportTypes;
}
