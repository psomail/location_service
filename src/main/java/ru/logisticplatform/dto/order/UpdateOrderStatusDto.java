package ru.logisticplatform.dto.order;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.dto.goods.GoodsForCreateOrderDto;
import ru.logisticplatform.model.order.OrderStatus;

import java.util.Date;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateOrderStatusDto {
    Long id;
    OrderStatus orderStatus;
}
