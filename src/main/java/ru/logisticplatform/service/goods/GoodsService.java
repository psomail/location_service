package ru.logisticplatform.service.goods;

import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.user.User;

import java.util.List;

public interface GoodsService {

    Goods findById(Long id);

    List<Goods> findAllByUser(User user);

    List<Goods> findAll();
}
