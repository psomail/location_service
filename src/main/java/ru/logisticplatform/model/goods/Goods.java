package ru.logisticplatform.model.goods;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "goods")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Goods extends BaseEntity {

    @Column(name = "name")
    String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goodstype_id")
    GoodsType goodsType;

    @Column(name = "lenght")
    Double lenght;

    @Column(name = "width")
    Double width;

    @Column(name = "height")
    Double height;

    @Column(name = "volume")
    Double volume;

    @Column(name = "carrying")
    Double carrying;

    @Enumerated(EnumType.STRING)
    @Column(name = "private")
    GoodsPrivate goodsPrivate = GoodsPrivate.NO;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToMany(mappedBy = "goods", fetch = FetchType.LAZY)
    List<Order> orders;

    @ManyToMany(mappedBy = "goods", fetch = FetchType.LAZY)
    List<Transportation> transportations;
}
