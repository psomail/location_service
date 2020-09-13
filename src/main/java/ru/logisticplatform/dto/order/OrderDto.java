package ru.logisticplatform.dto.order;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.dto.goods.GoodsForCreateOrderDto;

import java.util.Date;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {

    Long id;
    List<GoodsForCreateOrderDto> goods;
    Double latFrom;
    Double lonFrom;
    Double latTo;
    Double lonTo;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", timezone="Europe/Moscow")
    Date orderDate;
}
