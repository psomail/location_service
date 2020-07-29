package ru.logisticplatform.model.order;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.user.User;

import javax.persistence.*;

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


}
