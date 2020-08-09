package ru.logisticplatform.rest.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.goods.CreateGoodsDto;
import ru.logisticplatform.dto.goods.GoodsTypeDto;
import ru.logisticplatform.dto.goods.GoodsDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.goods.GoodsType;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.user.User;
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
    public ResponseEntity<GoodsDto> getGoods(@PathVariable("id") Long goodsId){

        if(goodsId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Goods goods = this.goodsService.findById(goodsId);

        if(goods == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GoodsDto goodsUserDto = ObjectMapperUtils.map(goods, GoodsDto.class);

        return new ResponseEntity<>(goodsUserDto, HttpStatus.OK);
    }

    /**
     *
     * @return
     */

    @GetMapping(value = "/goodstypes/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GoodsTypeDto>> getAllGoodsType(){

        List<GoodsType> goodsTypes = this.goodsTypeService.findAll();

        if(goodsTypes.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<GoodsTypeDto> goodsTypeUserDtos = ObjectMapperUtils.mapAll(goodsTypes, GoodsTypeDto.class);

        return new ResponseEntity<>(goodsTypeUserDtos, HttpStatus.OK);
    }

    /**
     *
     * @param goodsTypeId
     * @return
     */

    @GetMapping(value = "/goodstypes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsTypeDto> getGoodsType(@PathVariable("id") Long goodsTypeId){

        if(goodsTypeId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GoodsType goodsType = this.goodsTypeService.findById(goodsTypeId);

        if(goodsType == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GoodsTypeDto goodsTypeUserDto = ObjectMapperUtils.map(goodsType, GoodsTypeDto.class);

        return new ResponseEntity<>(goodsTypeUserDto, HttpStatus.OK);
    }

    @PostMapping(value = "/create/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateGoodsDto> createGoods(@RequestBody CreateGoodsDto goodsDto){
        HttpHeaders headers = new HttpHeaders();

        if(goodsDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(this.goodsService.findByGoodsDto(goodsDto) != null){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }

        Goods goods = ObjectMapperUtils.map(goodsDto, Goods.class);

        this.goodsService.createGoods(goods);

        return new ResponseEntity<>(goodsDto, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/test/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateGoodsDto> getGoodsTest(@PathVariable("id") Long goodsId){

        if(goodsId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Goods goods = this.goodsService.findById(goodsId);

        if(goods == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CreateGoodsDto goodsUserDto = ObjectMapperUtils.map(goods, CreateGoodsDto.class);

        return new ResponseEntity<>(goodsUserDto, HttpStatus.OK);
    }
}
