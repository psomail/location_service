package ru.logisticplatform.model.order;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "goodstypes")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoodsType extends BaseEntity {

    @Column(name = "name")
    String name;

    @OneToMany(mappedBy = "goodsType", fetch = FetchType.LAZY)
    List<Goods> goods;
}
