package ru.logisticplatform.model.goods;

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
    //@JsonIgnore
    List<Goods> goods;

    @Override
    public String toString() {
        return "Goods Type{" +
                "id: " + super.getId() + ", " +
                "name: " + name + "}";
    }
}
