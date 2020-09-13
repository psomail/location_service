package ru.logisticplatform.rest.transportation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.RestMessageDto;
import ru.logisticplatform.dto.goods.GoodsTypeDto;
import ru.logisticplatform.dto.goods.GoodsTypeForCreateGoodsDto;
import ru.logisticplatform.dto.transportation.TransportationCreateDto;
import ru.logisticplatform.dto.transportation.TransportationDto;
import ru.logisticplatform.dto.transportation.TransportationUpdateDto;
import ru.logisticplatform.dto.transportation.TransportationUpdateStatusDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestMessage;
import ru.logisticplatform.model.goods.GoodsType;
import ru.logisticplatform.model.transportation.TransportType;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.service.RestMessageService;
import ru.logisticplatform.service.goods.GoodsTypeService;
import ru.logisticplatform.service.transportation.TransportTypeService;
import ru.logisticplatform.service.transportation.TransportationService;
import ru.logisticplatform.service.user.RoleService;
import ru.logisticplatform.service.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for {@link Transportation} connected requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */


@RestController
@RequestMapping("/api/v1/transportations/")
public class TransportationRestControllerV1 {

    private final TransportationService transportationService;
    private final TransportTypeService transportTypeService;
    private final GoodsTypeService goodsTypeService;
    private final RestMessageService restMessageService;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public TransportationRestControllerV1(TransportationService transportationService
                                            , TransportTypeService transportTypeService
                                            , GoodsTypeService goodsTypeService
                                            , RestMessageService restMessageService
                                            , UserService userService
                                            , RoleService roleService){

        this.transportationService = transportationService;
        this.transportTypeService = transportTypeService;
        this.goodsTypeService = goodsTypeService;
        this.restMessageService = restMessageService;
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * 
     * @param transportationId
     * @return
     */

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransportaionById(@PathVariable("id") Long transportationId, Authentication authentication){

        Transportation transportation = transportationService.findById(transportationId);

        User user = this.userService.findByUsername(authentication.getName());

        if(transportation == null   || transportation.getTransportationStatus() == TransportationStatus.DELETED
                                    || user == null
                                    || user.getUserStatus() == UserStatus.DELETED
                                    ||!user.equals(transportation.getUser())){

            RestMessage restMessage = restMessageService.findByCode("T002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        TransportationDto transportationDto = ObjectMapperUtils.map(transportation, TransportationDto.class);

        return new ResponseEntity<TransportationDto>(transportationDto, HttpStatus.OK);
    }

    /**
     *
     * @param authentication
     * @return
     */

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransportaionsByUser(Authentication authentication){

        User user = userService.findByUsername(authentication.getName());

        if ( user == null   || user.getUserStatus() == UserStatus.DELETED
                            || !roleService.findUserRole(user, "ROLE_CONTRACTOR")){
            RestMessage restMessage = restMessageService.findByCode("T002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<Transportation> transportations = transportationService.findAllByUserAndStatusNotLike(user
                                                                                        ,TransportationStatus.DELETED);

        if(transportations == null || transportations.isEmpty()){
            RestMessage restMessage = restMessageService.findByCode("T002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<TransportationDto> transportationsDto = ObjectMapperUtils.mapAll(transportations, TransportationDto.class);

        return new ResponseEntity<List<TransportationDto>>(transportationsDto, HttpStatus.OK);
    }

    /**
     *
     * @param transportationCreateDto
     * @param authentication
     * @return
     */
    @PostMapping(value = "/create/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTransportaion(@RequestBody TransportationCreateDto transportationCreateDto
                                                                ,Authentication authentication){

        if (transportationCreateDto == null) {

            RestMessage restMessage = restMessageService.findByCode("T003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED
                         || roleService.findUserRole(user, "ROLE_CUSTOMER")) {

            RestMessage restMessage = restMessageService.findByCode("U003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Transportation transportation = ObjectMapperUtils.map(transportationCreateDto, Transportation.class);
        TransportType transportType = transportTypeService.findById(transportationCreateDto.getTransportType().getId());

        List<Long> ids = transportationCreateDto.getGoodsType().stream()
                                                                .map(GoodsTypeForCreateGoodsDto::getId)
                                                                .collect(Collectors.toList());
        List<GoodsType> goodsTypes = goodsTypeService.findAllByIds(ids);

        if(transportType == null){
            RestMessage restMessage = restMessageService.findByCode("T001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        if(goodsTypes.isEmpty()){
            RestMessage restMessage = restMessageService.findByCode("G001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        transportation.setTransportType(transportType);
        transportation.setGoodsType(goodsTypes);
        transportation.setUser(user);

        if(this.transportationService.findByTransportTypeAndModelAndUserAndTransportationStatusNotLike(transportType
                                                                                    ,transportationCreateDto.getModel()
                                                                                    ,user
                                                                                    ,TransportationStatus.DELETED) != null){
            RestMessage restMessage = restMessageService.findByCode("T004");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.FOUND);
        }

        transportationService.createTransportation(transportation);
        TransportationDto transportationDto = ObjectMapperUtils.map(transportation, TransportationDto.class);

        return new ResponseEntity<TransportationDto>(transportationDto, HttpStatus.CREATED);
    }

    /**
     *
     * @param authentication
     * @param transportationUpdateDto
     * @return
     */

    @PutMapping(value = "/update/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTransportaion(Authentication authentication
                                                    ,@RequestBody TransportationUpdateDto transportationUpdateDto){

        if (transportationUpdateDto == null) {
            RestMessage restMessage = restMessageService.findByCode("S001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null
                || user.getUserStatus() == UserStatus.DELETED
                || roleService.findUserRole(user, "ROLE_CUSTOMER")) {

            RestMessage restMessage = restMessageService.findByCode("U003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Transportation transportation = transportationService.findById(transportationUpdateDto.getId());

        if(transportation == null
                            || transportation.getTransportationStatus() == TransportationStatus.DELETED
                            || user.getId() != transportation.getUser().getId()){

            RestMessage restMessage = restMessageService.findByCode("T002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        TransportType transportType = transportTypeService.findById(transportationUpdateDto.getTransportType().getId());

        List<Long> ids = transportationUpdateDto.getGoodsType().stream()
                                                                        .map(GoodsTypeForCreateGoodsDto::getId)
                                                                        .collect(Collectors.toList());
        List<GoodsType> goodsTypes = goodsTypeService.findAllByIds(ids);

        if(transportType == null){

            RestMessage restMessage = restMessageService.findByCode("T001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        if(goodsTypes.isEmpty()){
            RestMessage restMessage = restMessageService.findByCode("G001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        transportation.setTransportType(transportType);
        transportation.setGoodsType(goodsTypes);
        transportation.setModel(transportationUpdateDto.getModel());

        transportationService.updateTransportation(transportation);
        TransportationDto transportationDto = ObjectMapperUtils.map(transportation, TransportationDto.class);

        return new ResponseEntity<TransportationDto>(transportationDto, HttpStatus.OK);
    }

    /**
     *
     * @param authentication
     * @param transportationUpdateStatusDto
     * @return
     */

    @PutMapping(value = "/updatestatus/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTransportaionStatus(Authentication authentication
                                            ,@RequestBody TransportationUpdateStatusDto transportationUpdateStatusDto){

        if (transportationUpdateStatusDto == null
                || transportationUpdateStatusDto.getTransportationStatus() == TransportationStatus.DELETED) {

            RestMessage restMessage = restMessageService.findByCode("S001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null
                || user.getUserStatus() == UserStatus.DELETED
                || this.roleService.findUserRole(user, "ROLE_CUSTOMER")) {

            RestMessage restMessage = restMessageService.findByCode("U003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Transportation transportation = transportationService.findById(transportationUpdateStatusDto.getId());

        if(transportation == null
                            || transportation.getTransportationStatus() == TransportationStatus.DELETED
                            || user.getId() != transportation.getUser().getId()){

            RestMessage restMessage = restMessageService.findByCode("T002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        TransportationStatus transportationStatus = transportationUpdateStatusDto.getTransportationStatus();

        if(transportationStatus == TransportationStatus.ACTIVE
                && !transportationService.findAllByUserAndStatus(user, TransportationStatus.ACTIVE).isEmpty()){

            RestMessage restMessage = restMessageService.findByCode("T008");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.FOUND);
        }

        transportation.setTransportationStatus(transportationStatus);
        this.transportationService.updateTransportation(transportation);
        TransportationDto transportationDto = ObjectMapperUtils.map(transportation, TransportationDto.class);

        return new ResponseEntity<TransportationDto>(transportationDto, HttpStatus.OK);
    }


    /**
     *
     * @param authentication
     * @param transportTypeId
     * @return
     */

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTransportaion(Authentication authentication, @PathVariable("id") Long transportTypeId){

        if(transportTypeId == null){

            RestMessage restMessage = restMessageService.findByCode("S001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(authentication.getName());
        if (user == null
                || user.getUserStatus() == UserStatus.DELETED
                || this.roleService.findUserRole(user, "ROLE_CUSTOMER")) {

            RestMessage restMessage = restMessageService.findByCode("U003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Transportation transportation = transportationService.findById(transportTypeId);
        if(transportation == null
                || transportation.getTransportationStatus() == TransportationStatus.DELETED
                || user.getId() != transportation.getUser().getId()){

            RestMessage restMessage = restMessageService.findByCode("T002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        transportation.setTransportationStatus(TransportationStatus.DELETED);

        this.transportationService.updateTransportation(transportation);

        RestMessage restMessage = restMessageService.findByCode("T005");
        RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

        return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.OK);
    }

}
