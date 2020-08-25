package ru.logisticplatform.rest.transportation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.RestErrorDto;
import ru.logisticplatform.dto.transportation.TransportTypeDto;
import ru.logisticplatform.dto.transportation.TransportationCreateDto;
import ru.logisticplatform.dto.transportation.TransportationDto;
import ru.logisticplatform.dto.user.SignUpUserDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestError;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.transportation.TransportType;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.service.RestErrorService;
import ru.logisticplatform.service.transportation.TransportTypeService;
import ru.logisticplatform.service.transportation.TransportationService;
import ru.logisticplatform.service.user.RoleService;
import ru.logisticplatform.service.user.UserService;

import java.util.List;

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
    private final RestErrorService restErrorService;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public TransportationRestControllerV1(TransportationService transportationService
                                            ,TransportTypeService transportTypeService
                                            ,RestErrorService restErrorService
                                            ,UserService userService
                                            ,RoleService roleService){

        this.transportationService = transportationService;
        this.transportTypeService = transportTypeService;
        this.restErrorService = restErrorService;
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

        if (transportationId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Transportation transportation = this.transportationService.findById(transportationId);

        User user = this.userService.findByUsername(authentication.getName());

        if(transportation == null   || transportation.getTransportationStatus() == TransportationStatus.DELETED
                                    || user == null
                                    || user.getUserStatus() == UserStatus.DELETED
                                    ||!user.equals(transportation.getUser())){

            RestError restError = this.restErrorService.findByCode("T002");
            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);

            return new ResponseEntity<RestErrorDto>(restErrorDto, HttpStatus.NOT_FOUND);
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

        User user = this.userService.findByUsername(authentication.getName());

        if ( user == null   || user.getUserStatus() == UserStatus.DELETED
                            || !this.roleService.findUserRole(user, "ROLE_CONTRACTOR")){

            RestError restError = this.restErrorService.findByCode("T002");
            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);

            return new ResponseEntity<RestErrorDto>(restErrorDto, HttpStatus.NOT_FOUND);
        }

        List<Transportation> transportations = this.transportationService.findAllByUserAndStatusNotLike(user
                                                                                        ,TransportationStatus.DELETED);

        if(transportations == null || transportations.isEmpty()){

            RestError restError = this.restErrorService.findByCode("T002");
            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);

            return new ResponseEntity<RestErrorDto>(restErrorDto, HttpStatus.NOT_FOUND);
        }

        List<TransportationDto> transportationsDto = ObjectMapperUtils.mapAll(transportations, TransportationDto.class);

        return new ResponseEntity<List<TransportationDto>>(transportationsDto, HttpStatus.OK);
    }


    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTransportaion(@RequestBody TransportationCreateDto transportationCreate){

        if (transportationCreate == null) {

            RestError restError = this.restErrorService.findByCode("T003");
            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);
            return new ResponseEntity<RestErrorDto>(restErrorDto, HttpStatus.NOT_FOUND);
        }

//        if(this.transportationService.findByTransportation(transportationCreate) != null) {
//
//            RestError restError = this.restErrorService.findByCode("T004");
//            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);
//            return new ResponseEntity<RestErrorDto>(HttpStatus.FOUND);
//        }


        return null;
    }


    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTransportaion(@PathVariable("id") Long transportTypeId){

        if(transportTypeId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Transportation transportation = this.transportationService.findById(transportTypeId);

        if (transportation == null) {

            RestError restError = this.restErrorService.findByCode("T002");
            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);

            return new ResponseEntity<RestErrorDto>(restErrorDto, HttpStatus.NOT_FOUND);
        }
        transportation.setTransportationStatus(TransportationStatus.DELETED);

        this.transportationService.updateTransportation(transportation);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
