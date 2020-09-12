package ru.logisticplatform.service.goods.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.goods.GoodsType;
import ru.logisticplatform.service.goods.GoodsTypeService;
import ru.logisticplatform.repository.goods.GoodsTypeRepository;

import java.util.List;

/**
 * Implementation of {@link GoodsTypeService} interface.
 * Wrapper for {@link GoodsTypeRepository} + business logic.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@Service
@Slf4j
public class GoodsTypeServiceImpl implements GoodsTypeService {

    private final GoodsTypeRepository goodsTypeRepository;

    @Autowired
    public GoodsTypeServiceImpl(GoodsTypeRepository goodsTypeRepository) {
        this.goodsTypeRepository = goodsTypeRepository;
    }


    @Override
    public GoodsType findById(Long id) {
        GoodsType goodsType = goodsTypeRepository.findById(id).orElse(null);

        if (goodsType == null){
            log.warn("IN GoodsTypeServiceImpl findById - no goodsType found by id: {}", id);
            return null;
        }

        log.info("IN GoodsTypeServiceImpl findById - goodsType: {} found by id: {}", goodsType, id);

        return goodsType;
    }

    @Override
    public List<GoodsType> findAll() {
        log.info("IN GoodsTypeServiceImpl findAll");

        return goodsTypeRepository.findAll();
    }

    /**
     *
     * @param ids
     * @return
     */
    @Override
    public List<GoodsType> findAllByIds(List<Long> ids){

        List<GoodsType> goodsTypes = goodsTypeRepository.findAllByIdIn(ids);

        if (goodsTypes.isEmpty()){
            log.warn("IN GoodsTypeServiceImpl findAllByIds() - no goodsType found by id list");
        }
        return goodsTypes;
    }
}
