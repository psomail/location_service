package ru.logisticplatform.repository.goods;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.goods.GoodsType;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link GoodsType}.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

public interface GoodsTypeRepository extends JpaRepository<GoodsType, Long> {

}