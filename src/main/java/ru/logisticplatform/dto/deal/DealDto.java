package ru.logisticplatform.dto.deal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.dto.order.OrderDto;
import ru.logisticplatform.dto.transportation.TransportationDto;
import ru.logisticplatform.model.deal.DealConfirmStatus;
import ru.logisticplatform.model.deal.DealStatus;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.transportation.Transportation;

import java.util.Date;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DealDto {

    Long id;
    OrderDto order;
    TransportationDto transportation;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", timezone="Europe/Moscow")
    Date dealDate;

    DealConfirmStatus dealCustomerConfirm;
    DealConfirmStatus dealContractorConfirm;
    DealStatus dealStatus;

}
