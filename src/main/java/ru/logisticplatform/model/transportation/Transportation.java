package ru.logisticplatform.model.transportation;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.deal.Deal;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "transportations")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transportation extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transport_type_id")
    TransportType transportType;

    @Column(name = "model")
    String model;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "transportations_goods",
            joinColumns = {@JoinColumn(name = "transportations_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "goods_id", referencedColumnName = "id")})
    List<Goods> goods;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    TransportationStatus transportationStatus = TransportationStatus.CREATED;

    @OneToMany(mappedBy = "transportation", fetch = FetchType.LAZY)
    List<Deal> deals;
}
