package ru.logisticplatform.rest.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.RestMessageDto;
import ru.logisticplatform.dto.goods.CreateGoodsDto;
import ru.logisticplatform.dto.goods.GoodsTypeDto;
import ru.logisticplatform.dto.goods.GoodsDto;
import ru.logisticplatform.dto.goods.GoodsUpdateStatusDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestMessage;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.goods.GoodsPrivate;
import ru.logisticplatform.model.goods.GoodsStatus;
import ru.logisticplatform.model.goods.GoodsType;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.service.RestMessageService;
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
@RequestMapping("/api/v1/goods/")
public class GoodsRestControllerV1 {

    private final GoodsService goodsService;
    private final GoodsTypeService goodsTypeService;
    private final UserService userService;
    private final RestMessageService restMessageService;

    @Autowired
    public GoodsRestControllerV1(GoodsService goodsService
                                ,GoodsTypeService goodsTypeService
                                ,UserService userService
                                ,RestMessageService restMessageService) {
        this.goodsService = goodsService;
        this.goodsTypeService = goodsTypeService;
        this.userService = userService;
        this.restMessageService = restMessageService;
    }

    /**
     *
     * @param authentication
     * @return
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getGoodsByUser(Authentication authentication){

        User user = this.userService.findByUsername(authentication.getName());

        if ( user == null || user.getUserStatus() == UserStatus.DELETED){

            RestMessage restMessage = this.restMessageService.findByCode("U001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<Goods> goods = goodsService.findAllByUserAndStatusNotLike(user, GoodsStatus.DELETED);

        if(goods == null || goods.isEmpty()){

            RestMessage restMessage = this.restMessageService.findByCode("G002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<GoodsDto> goodsDto = ObjectMapperUtils.mapAll(goods, GoodsDto.class);

        return new ResponseEntity<List<GoodsDto>>(goodsDto, HttpStatus.OK);
    }

    /**
     *
     * @param authentication
     * @param goodsId
     * @return
     */

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> getGoods(Authentication authentication, @PathVariable("id") Long goodsId){

        Goods goods = this.goodsService.findById(goodsId);

        User user = this.userService.findByUsername(authentication.getName());

        if(goods == null    || goods.getGoodsStatus() == GoodsStatus.DELETED
                            || user == null
                            || user.getUserStatus() == UserStatus.DELETED
                            ||!user.equals(goods.getUser())){

            RestMessage restMessage = this.restMessageService.findByCode("G002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        GoodsDto goodsDto = ObjectMapperUtils.map(goods, GoodsDto.class);

        return new ResponseEntity<GoodsDto>(goodsDto, HttpStatus.OK);
    }

    /**
     *
     * @param goodsDto
     * @return
     */

    @PostMapping(value = "/create/", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createGoods(Authentication authentication, @RequestBody CreateGoodsDto goodsDto){
        HttpHeaders headers = new HttpHeaders();

        if(goodsDto == null) {

            RestMessage restMessage = this.restMessageService.findByCode("G003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {

            RestMessage restMessage = this.restMessageService.findByCode("U001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        GoodsType goodsType = goodsTypeService.findById(goodsDto.getGoodsType().getId());
        if(goodsType ==null){

            RestMessage restMessage = this.restMessageService.findByCode("G001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        if(goodsService.findByGoodsDtoAndUser(goodsDto, user) != null){

            RestMessage restMessage = this.restMessageService.findByCode("G004");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.FOUND);
        }

        Goods goods = ObjectMapperUtils.map(goodsDto, Goods.class);

        goods.setUser(user);
        goods.setGoodsStatus(GoodsStatus.CREATED);

        this.goodsService.createGoods(goods);

        GoodsDto createdGoodsDto = ObjectMapperUtils.map(goods, GoodsDto.class);

        return new ResponseEntity<GoodsDto>(createdGoodsDto, headers, HttpStatus.CREATED);
    }

    /**
     *
     * @param goodsDto
     * @return
     */

    @PutMapping(value = "/update/", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateGoods(Authentication authentication, @RequestBody GoodsDto goodsDto){
        HttpHeaders headers = new HttpHeaders();

        if(goodsDto == null) {

            RestMessage restMessage = this.restMessageService.findByCode("S001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {

            RestMessage restMessage = this.restMessageService.findByCode("U001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Goods goods = this.goodsService.findById(goodsDto.getId());

        if (goods == null
                        ||goods.getGoodsStatus() == GoodsStatus.DELETED
                        ||user.getId() != goods.getUser().getId()){

            RestMessage restMessage = this.restMessageService.findByCode("G002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        GoodsType goodsType = goodsTypeService.findById(goodsDto.getGoodsType().getId());

        if(goodsType == null){

            RestMessage restMessage = this.restMessageService.findByCode("G001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        goods.setName(goodsDto.getName());
        goods.setGoodsType(goodsType);
        goods.setLenght(goodsDto.getLenght());
        goods.setWidth(goodsDto.getWidth());
        goods.setHeight(goodsDto.getHeight());
        goods.setVolume(goodsDto.getVolume());
        goods.setCarrying(goodsDto.getCarrying());

        this.goodsService.updateGoods(goods);

        return new ResponseEntity<>(goodsDto, headers, HttpStatus.OK);
    }

    @PutMapping(value = "/updatestatus/", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateGoodsStatus(Authentication authentication, @RequestBody GoodsUpdateStatusDto goodsUpdateStatusDto){
        HttpHeaders headers = new HttpHeaders();

        if(goodsUpdateStatusDto == null) {

            RestMessage restMessage = this.restMessageService.findByCode("S001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {

            RestMessage restMessage = this.restMessageService.findByCode("U001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Goods goods = this.goodsService.findById(goodsUpdateStatusDto.getId());

        if (goods == null
                ||goods.getGoodsStatus() == GoodsStatus.DELETED
                ||user.getId() != goods.getUser().getId()){

            RestMessage restMessage = this.restMessageService.findByCode("G002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        GoodsStatus goodsStatus = goodsUpdateStatusDto.getGoodsStatus();

        if(goodsStatus == GoodsStatus.ACTIVE
                    && !goodsService.findAllByUserAndStatus(user, GoodsStatus.ACTIVE).isEmpty()){

            RestMessage restMessage = this.restMessageService.findByCode("G005");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.FOUND);
        }

        goods.setGoodsStatus(goodsStatus);

        goodsService.updateGoods(goods);

        GoodsDto goodsDto = ObjectMapperUtils.map(goods, GoodsDto.class);

        return new ResponseEntity<GoodsDto>(goodsDto, headers, HttpStatus.OK);
    }


    /**
     *
     * @param goodsId
     * @return
     */

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> deleteGoods(Authentication authentication, @PathVariable("id") Long goodsId){
        HttpHeaders headers = new HttpHeaders();

        if(goodsId == null) {

            RestMessage restMessage = this.restMessageService.findByCode("S001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(authentication.getName());
        if (user == null || user.getUserStatus() == UserStatus.DELETED) {

            RestMessage restMessage = this.restMessageService.findByCode("U001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Goods goods = this.goodsService.findById(goodsId);

        if (goods == null
                ||goods.getGoodsStatus() == GoodsStatus.DELETED
                ||user.getId() != goods.getUser().getId()){

            RestMessage restMessage = this.restMessageService.findByCode("G002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        goods.setGoodsStatus(GoodsStatus.DELETED);

        goodsService.updateGoods(goods);

        RestMessage restMessage = this.restMessageService.findByCode("G006");
        RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

        return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.OK);
    }


    /**
     *
     * @param goodsId
     * @return
     */

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
