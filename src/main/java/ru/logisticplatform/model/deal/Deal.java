package ru.logisticplatform.model.deal;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.transportation.Transportation;

import javax.persistence.*;


@Entity
@Table(name = "deals")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Deal extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transportation_id")
    Transportation transportation;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    DealStatus dealStatus = DealStatus.CREATED;
}
