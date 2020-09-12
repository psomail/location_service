package ru.logisticplatform.dto.transportation;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.dto.goods.GoodsTypeForCreateGoodsDto;
import ru.logisticplatform.model.transportation.TransportationStatus;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportationUpdateDto {

    Long id;
    TransportTypeCreateTransportationDto transportType;
    String model;
    List<GoodsTypeForCreateGoodsDto> goodsType;
}
