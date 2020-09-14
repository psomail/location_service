package ru.logisticplatform.dto.deal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.logisticplatform.dto.order.OrderForCreateDealDto;
import ru.logisticplatform.dto.transportation.TransportationForCreateDealDto;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.deal.DealConfirmStatus;
import ru.logisticplatform.model.deal.DealStatus;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.transportation.Transportation;

import javax.persistence.*;
import java.util.Date;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateDealDto {
    OrderForCreateDealDto order;
    TransportationForCreateDealDto transportation;
}
