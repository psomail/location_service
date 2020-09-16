package ru.logisticplatform.model.goods;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.BaseEntity;
import ru.logisticplatform.model.transportation.Transportation;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "goodstypes")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoodsType extends BaseEntity {

    @Column(name = "name")
    String name;

    @ManyToMany(mappedBy = "goodsType", fetch = FetchType.LAZY)
    List<Transportation> transportations;

    @Override
    public String toString() {
        return "Goods Type{" +
                "id: " + super.getId() + ", " +
                "name: " + name + "}";
    }
}
