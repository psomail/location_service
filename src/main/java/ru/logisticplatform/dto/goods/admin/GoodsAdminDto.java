package ru.logisticplatform.dto.goods.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.dto.user.admin.AdminUserDto;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.goods.GoodsPrivate;

import java.util.Date;

/**
 * DTO class for user requests by {@link Goods}
 *
 * @author Sergei Perminov
 * @version 1.0
 */


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsAdminDto {

    Long id;
    GoodsTypeAdminDto goodsType;
    String name;
    Double lenght;
    Double width;
    Double height;
    Double volume;
    Double carrying;
    GoodsPrivate goodsPrivate;
    AdminUserDto user;
    Date created;
    Date updated;

}