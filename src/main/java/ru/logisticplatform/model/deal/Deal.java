package ru.logisticplatform.model.deal;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.transportation.Transportation;

import javax.persistence.*;
import java.util.Date;


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

    @DateTimeFormat
    @Column(name = "deal_date")
    Date dealDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_confirm")
    DealConfirmStatus dealCustomerConfirm = DealConfirmStatus.NO;

    @Enumerated(EnumType.STRING)
    @Column(name = "contractor_confirm")
    DealConfirmStatus dealContractorConfirm = DealConfirmStatus.NO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    DealStatus dealStatus = DealStatus.CREATED;


    @Override
    public String toString() {
        return "Deal{" +
               // "order=" + order +
               // ", transportation=" + transportation +
                ", dealDate=" + dealDate +
                ", dealCustomerConfirm=" + dealCustomerConfirm +
                ", dealContractorConfirm=" + dealContractorConfirm +
                ", dealStatus=" + dealStatus +
                '}';
    }
}
