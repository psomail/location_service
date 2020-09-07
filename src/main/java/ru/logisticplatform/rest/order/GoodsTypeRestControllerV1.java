package ru.logisticplatform.rest.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.RestMessageDto;
import ru.logisticplatform.dto.goods.CreateGoodsDto;
import ru.logisticplatform.dto.goods.GoodsDto;
import ru.logisticplatform.dto.goods.GoodsTypeDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestMessage;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.goods.GoodsStatus;
import ru.logisticplatform.model.goods.GoodsType;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.service.RestMessageService;
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
@RequestMapping("/api/v1/goodstypes/")
public class GoodsTypeRestControllerV1 {

    private final GoodsService goodsService;
    private final GoodsTypeService goodsTypeService;
    private final RestMessageService restMessageService;

    @Autowired
    public GoodsTypeRestControllerV1(GoodsService goodsService
                                    ,GoodsTypeService goodsTypeService
                                    ,RestMessageService restMessageService) {
        this.goodsService = goodsService;
        this.goodsTypeService = goodsTypeService;
        this.restMessageService = restMessageService;
    }

    /**
     *
     * @return
     */

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('CUSTOMER, CONTRACTOR')")
    public ResponseEntity<?> getAllGoodsType(){

        List<GoodsType> goodsTypes = this.goodsTypeService.findAll();

        if(goodsTypes.isEmpty()){

            RestMessage restMessage = this.restMessageService.findByCode("G001");;
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<GoodsTypeDto> goodsTypeUserDto = ObjectMapperUtils.mapAll(goodsTypes, GoodsTypeDto.class);

        return new ResponseEntity<>(goodsTypeUserDto, HttpStatus.OK);
    }


    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('CUSTOMER, CONTRACTOR')")
    public ResponseEntity<?> getGoodsType(@PathVariable("id") Long goodsTypeId){

//        if(goodsTypeId == null){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }

        GoodsType goodsType = this.goodsTypeService.findById(goodsTypeId);

        if(goodsType == null){

            RestMessage restMessage = this.restMessageService.findByCode("G001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        GoodsTypeDto goodsTypeUserDto = ObjectMapperUtils.map(goodsType, GoodsTypeDto.class);

        return new ResponseEntity<GoodsTypeDto>(goodsTypeUserDto, HttpStatus.OK);
    }
}
