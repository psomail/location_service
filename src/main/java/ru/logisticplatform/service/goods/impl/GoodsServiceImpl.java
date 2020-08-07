package ru.logisticplatform.service.goods.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.repository.goods.GoodsRepository;
import ru.logisticplatform.service.goods.GoodsService;

import java.util.List;


@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository ) {
        this.goodsRepository = goodsRepository;
    }

    @Override
    public Goods findById(Long id) {

        Goods goods = goodsRepository.findById(id).orElse(null);

        if(goods == null){
            log.warn("IN GoodsServiceImpl findById - no goods found by id: {}", id);
            return null;
        }

        log.info("IN GoodsServiceImpl findById - goods: {} found by id: {}", goods, id);

        return goods;
    }

    @Override
    public List<Goods> findAllByUser(User user) {

        List<Goods> goods = goodsRepository.findAllByUser(user);

        if(goods.isEmpty()){
            log.warn("IN GoodsServiceImpl findAllByUser - no goods found by user: {}", user);
            return null;
        }

        log.info("IN GoodsServiceImpl findAllByUser - goods: {} found by user: {}", goods, user);

        return goods;
    }

    @Override
    public List<Goods> findAll() {
        log.info("IN GoodsServiceImpl findAll");
        return goodsRepository.findAll();
    }
}
