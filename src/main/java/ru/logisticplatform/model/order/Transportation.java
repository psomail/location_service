package ru.logisticplatform.model.order;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.user.User;

import javax.persistence.*;


@Entity
@Table(name = "transportations")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transportation extends BaseEntity {

    @Column(name = "name")
    String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transport_type_id")
    TransportType transportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    TransportationStatus transportationStatus = TransportationStatus.CREATED;

}
