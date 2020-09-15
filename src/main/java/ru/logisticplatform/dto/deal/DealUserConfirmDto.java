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

import java.util.Date;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DealUserConfirmDto {

    Long dealId;
    DealConfirmStatus dealUserConfirm;
}
