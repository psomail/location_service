package ru.logisticplatform.service.goods.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.dto.goods.CreateGoodsDto;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.goods.GoodsType;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.repository.goods.GoodsRepository;
import ru.logisticplatform.repository.user.UserRepository;
import ru.logisticplatform.service.goods.GoodsService;
import ru.logisticplatform.service.goods.GoodsTypeService;
import ru.logisticplatform.service.user.UserService;

import java.util.List;


@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;
    private final UserService userService;
    private final GoodsTypeService goodsTypeService;

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository,  UserService userService, GoodsTypeService goodsTypeService) {
        this.goodsRepository = goodsRepository;
        this.userService = userService;
        this.goodsTypeService = goodsTypeService;
    }


    /**
     *
     * @param id
     * @return
     */

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


    /**
     *
     * @param goodsDto
     * @return
     */

    @Override
    public List<Goods> findByGoodsDto(CreateGoodsDto goodsDto) {

        User user = userService.findById(goodsDto.getUser().getId());
        GoodsType goodsType = goodsTypeService.findById(goodsDto.getGoodsType().getId());

        List<Goods> goods = goodsRepository.findAllByNameAndGoodsTypeAndLenghtAndWidthAndHeightAndVolumeAndCarryingAndUser(goodsDto.getName()
                                                                                                                            ,goodsType
                                                                                                                            ,goodsDto.getLenght()
                                                                                                                            ,goodsDto.getWidth()
                                                                                                                            ,goodsDto.getHeight()
                                                                                                                            ,goodsDto.getVolume()
                                                                                                                            ,goodsDto.getCarrying()
                                                                                                                            ,user);
        if(goods.isEmpty()) {
            log.warn("IN GoodsServiceImpl findByGoodsDto - no goods found by goodsDto: {}", goodsDto);
            return null;
        }
        log.info("IN GoodsServiceImpl findByGoodsDto - amount of goods: {} found by goodsDto: {}", goods.size(), goodsDto);

        return goods;
    }

    /**
     *
     * @param user
     * @return
     */

    @Override
    public List<Goods> findAllByUser(User user) {

        List<Goods> goods = goodsRepository.findAllByUser(user);

        if(goods.isEmpty()){
            log.warn("IN GoodsServiceImpl findAllByUser - no goods found by user: {}", user);
            return null;
        }

        log.info("IN GoodsServiceImpl findAllByUser - amount of goods: {} found by user: {}", goods.size(), user);

        return goods;
    }

    /**
     *
     * @return
     */

    @Override
    public List<Goods> findAll() {
        log.info("IN GoodsServiceImpl findAll");
        return goodsRepository.findAll();
    }


    /**
     *
     * @param goods
     * @return
     */

    @Override
    public Goods createGoods(Goods goods) {

        Goods createdGoods = goodsRepository.save(goods);

        log.info("IN GoodsServiceImpl createGoods - goods: {} successfully registered", createdGoods);

        return createdGoods;
    }
}
