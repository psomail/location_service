package ru.logisticplatform.rest.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.logisticplatform.dto.goods.GoodsTypeUserDto;
import ru.logisticplatform.dto.goods.GoodsUserDto;
import ru.logisticplatform.dto.goods.GoodsTypeDto;
import ru.logisticplatform.dto.goods.admin.GoodsTypeAdminDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.goods.GoodsType;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.service.goods.GoodsService;
import ru.logisticplatform.service.goods.GoodsTypeService;

import java.util.List;

/**
 * REST controller for {@link Order} connected requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@RestController
@RequestMapping("/api/v1/goods/")
public class GoodsRestControllerV1 {

    private final GoodsService goodsService;
    private final GoodsTypeService goodsTypeService;

    @Autowired
    public GoodsRestControllerV1(GoodsService goodsService, GoodsTypeService goodsTypeService) {
        this.goodsService = goodsService;
        this.goodsTypeService = goodsTypeService;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsUserDto> getGoods(@PathVariable("id") Long goodsId){

        if(goodsId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Goods goods = this.goodsService.findById(goodsId);

        if(goods == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GoodsUserDto goodsUserDto = ObjectMapperUtils.map(goods, GoodsUserDto.class);

        return new ResponseEntity<>(goodsUserDto, HttpStatus.OK);
    }


    @GetMapping(value = "/goodstypes/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GoodsTypeUserDto>> getAllGoodsType(){

        List<GoodsType> goodsTypes = this.goodsTypeService.findAll();

        if(goodsTypes.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<GoodsTypeUserDto> goodsTypeUserDtos = ObjectMapperUtils.mapAll(goodsTypes, GoodsTypeUserDto.class);

        return new ResponseEntity<>(goodsTypeUserDtos, HttpStatus.OK);
    }


    @GetMapping(value = "/goodstypes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsTypeUserDto> getGoodsType(@PathVariable("id") Long goodsTypeId){

        if(goodsTypeId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GoodsType goodsType = this.goodsTypeService.findById(goodsTypeId);

        if(goodsType == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GoodsTypeUserDto goodsTypeUserDto = ObjectMapperUtils.map(goodsType, GoodsTypeUserDto.class);

        return new ResponseEntity<>(goodsTypeUserDto, HttpStatus.OK);
    }
}
