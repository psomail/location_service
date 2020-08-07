package ru.logisticplatform.service.goods;

import ru.logisticplatform.model.goods.GoodsType;

import java.util.List;

public interface GoodsTypeService {

    GoodsType findById(Long id);

    List<GoodsType> findAll();
}
