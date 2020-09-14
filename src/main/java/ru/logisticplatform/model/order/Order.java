package ru.logisticplatform.model.order;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.deal.Deal;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.user.User;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "orders_goods",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "goods_id", referencedColumnName = "id")})
    List<Goods> goods;

    @Column(name = "lat_from")
    Double latFrom;

    @Column(name = "lon_from")
    Double lonFrom;

    @Column(name = "lat_to")
    Double latTo;

    @Column(name = "lon_to")
    Double lonTo;

    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    OrderStatus orderStatus = OrderStatus.CREATED;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    List<Deal> deals;


    @Override
    public String toString() {
        return "Order{" +
            //    "user=" + user +
             //   ", goods=" + goods +
                ", latFrom=" + latFrom +
                ", lonFrom=" + lonFrom +
                ", latTo=" + latTo +
                ", lonTo=" + lonTo +
                ", orderDate=" + orderDate +
                ", orderStatus=" + orderStatus +
            //    ", deals=" + deals +
                '}';
    }
}
