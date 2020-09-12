package ru.logisticplatform.dto.order;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.dto.goods.GoodsForCreateOrderDto;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.deal.Deal;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.order.OrderStatus;
import ru.logisticplatform.model.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOrderDto {

    List<GoodsForCreateOrderDto> goods;
    Double latFrom;
    Double lonFrom;
    Double latTo;
    Double lonTo;
    Date orderDate;
}
