package ru.logisticplatform.dto.goods;

import ru.logisticplatform.model.goods.Goods;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO class for user requests by {@link Goods}
 *
 * @author Sergei Perminov
 * @version 1.0
 */


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsDto {


    String name;
    GoodsTypeDto goodsType;
    Double lenght;
    Double width;
    Double height;
    Double volume;
    Double carrying;
}