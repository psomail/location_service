package ru.logisticplatform.dto.transportation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO class for user requests by Transport_TransportType
 *
 * @author Sergei Perminov
 * @version 1.0
 */


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportTypeDto {
    String name;
    String description;
    Double lenght;
    Double width;
    Double height;
    Double volume;
    Double carrying;
}
