package ru.logisticplatform.rest.order.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.logisticplatform.dto.goods.admin.GoodsAdminDto;
import ru.logisticplatform.dto.goods.admin.GoodsTypeAdminAllGoodsDto;
import ru.logisticplatform.dto.goods.admin.GoodsTypeAdminDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.goods.GoodsType;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.service.goods.GoodsService;
import ru.logisticplatform.service.goods.GoodsTypeService;
import ru.logisticplatform.service.user.UserService;

import java.util.List;

/**
 * REST controller for {@link Order} connected requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@RestController
@RequestMapping("/api/v1/admin/goods/")
public class GoodsRestControllerAdminV1 {

    private final GoodsService goodsService;
    private final GoodsTypeService goodsTypeService;
    private final UserService userService;

    @Autowired
    public GoodsRestControllerAdminV1(GoodsService goodsService, GoodsTypeService goodsTypeService, UserService userService) {
        this.goodsService = goodsService;
        this.goodsTypeService = goodsTypeService;
        this.userService = userService;
    }


    /**
     *
     * @param goodsId
     * @return
     */

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsAdminDto> getGoods(@PathVariable("id") Long goodsId){

        if(goodsId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Goods goods = this.goodsService.findById(goodsId);

        if(goods == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GoodsAdminDto goodsAdminDto = ObjectMapperUtils.map(goods, GoodsAdminDto.class);

        return new ResponseEntity<>(goodsAdminDto, HttpStatus.OK);
    }


    /**
     *
     * @return
     */

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GoodsAdminDto>> getAllGoods(){

        List<Goods> goods = this.goodsService.findAll();

        if(goods.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<GoodsAdminDto> goodsAdminDtos = ObjectMapperUtils.mapAll(goods, GoodsAdminDto.class);

        return new ResponseEntity<>(goodsAdminDtos, HttpStatus.OK);
    }


    /**
     *
     * @param userId
     * @return
     */

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GoodsAdminDto>> getAllGoodsByUserId(@PathVariable("id") Long userId){

        if(userId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user =  userService.findById(userId);

        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Goods> goods = goodsService.findAllByUser(user);

        List<GoodsAdminDto> goodsAdminDtos = ObjectMapperUtils.mapAll(goods, GoodsAdminDto.class);

        return new ResponseEntity<>(goodsAdminDtos, HttpStatus.OK);
    }


    /**
     *
     * @return
     */

    @GetMapping(value = "/goodstypes/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GoodsTypeAdminDto>> getAllGoodsType(){

        List<GoodsType> goodsTypes = this.goodsTypeService.findAll();

        if(goodsTypes.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<GoodsTypeAdminDto> goodsTypeAdminDtos = ObjectMapperUtils.mapAll(goodsTypes, GoodsTypeAdminDto.class);

        return new ResponseEntity<>(goodsTypeAdminDtos, HttpStatus.OK);
    }


    /**
     *
     * @param goodsTypeId
     * @return
     */

    @GetMapping(value = "/goodstypes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsTypeAdminDto> getGoodsType(@PathVariable("id") Long goodsTypeId){

        if(goodsTypeId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GoodsType goodsType = this.goodsTypeService.findById(goodsTypeId);

        if(goodsType == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GoodsTypeAdminDto goodsTypAdminDto = ObjectMapperUtils.map(goodsType, GoodsTypeAdminDto.class);

        return new ResponseEntity<>(goodsTypAdminDto, HttpStatus.OK);
    }


    /**
     *
     * @param goodsTypeId
     * @return
     */

    @GetMapping(value = "/goodstypes-allgoods/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsTypeAdminAllGoodsDto> getGoodsTypeAllGoods(@PathVariable("id") Long goodsTypeId){

        if(goodsTypeId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GoodsType goodsType = this.goodsTypeService.findById(goodsTypeId);

        if(goodsType == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GoodsTypeAdminAllGoodsDto goodsTypAdminDto = ObjectMapperUtils.map(goodsType, GoodsTypeAdminAllGoodsDto.class);

        return new ResponseEntity<>(goodsTypAdminDto, HttpStatus.OK);
    }
}
