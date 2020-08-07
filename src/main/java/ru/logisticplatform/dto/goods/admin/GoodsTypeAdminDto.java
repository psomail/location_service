package ru.logisticplatform.dto.goods.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.goods.GoodsType;

import java.util.Date;


/**
 * DTO class for user requests by {@link GoodsType}
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsTypeAdminDto {
    Long id;
    String name;
    Date created;
    Date updated;
}