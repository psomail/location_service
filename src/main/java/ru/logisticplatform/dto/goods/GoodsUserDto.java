package ru.logisticplatform.dto.goods;

import ru.logisticplatform.model.goods.Goods;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.goods.GoodsPrivate;

/**
 * DTO class for user requests by {@link Goods}
 *
 * @author Sergei Perminov
 * @version 1.0
 */


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsUserDto {

    GoodsTypeUserDto goodsType;
    String name;
    Double lenght;
    Double width;
    Double height;
    Double volume;
    Double carrying;
    GoodsPrivate goodsPrivate;
}