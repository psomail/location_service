package ru.logisticplatform.service.goods;

import ru.logisticplatform.dto.goods.CreateGoodsDto;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.goods.GoodsStatus;
import ru.logisticplatform.model.user.User;

import java.util.List;

public interface GoodsService {

    Goods findById(Long id);

    List<Goods> findByGoodsDtoAndUser(CreateGoodsDto goodsDto, User user);

    List<Goods> findAllByUser(User user);

    List<Goods> findAllByUserAndStatusNotLike(User user, GoodsStatus status);

    List<Goods> findAllByUserAndStatus(User user, GoodsStatus status);

    List<Goods> findAll();

    Goods createGoods(Goods goods);

    Goods updateGoods(Goods goods);

    void delete(Long id);
}
