package ru.logisticplatform.repository.goods;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.logisticplatform.dto.goods.GoodsTypeForCreateGoodsDto;
import ru.logisticplatform.dto.goods.UserForCreateGoodsDto;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.goods.GoodsStatus;
import ru.logisticplatform.model.goods.GoodsType;
import ru.logisticplatform.model.user.User;

import java.util.List;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link Goods}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    List<Goods> findAllByUser(User user);

    List<Goods> findAllByNameAndGoodsTypeAndLenghtAndWidthAndHeightAndVolumeAndCarryingAndUser(String name
                                                                                                ,GoodsType goodsType
                                                                                                ,Double lenght
                                                                                                ,Double width
                                                                                                ,Double height
                                                                                                ,Double volume
                                                                                                ,Double carrying
                                                                                                ,User user);
    List<Goods> findAllByUserAndGoodsStatus(User user, GoodsStatus status);

    List<Goods> findAllByUserAndGoodsStatusNotLike(User user, GoodsStatus status);
 }
