package ru.logisticplatform.dto.goods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.logisticplatform.model.goods.GoodsType;

import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsTypeForCreateGoodsDto {
    Long id;
}