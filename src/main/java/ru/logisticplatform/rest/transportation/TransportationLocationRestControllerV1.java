package ru.logisticplatform.rest.transportation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.RestMessageDto;
import ru.logisticplatform.dto.transportation.TransportationDto;
import ru.logisticplatform.dto.transportation.TransportationLocationDto;
import ru.logisticplatform.dto.transportation.TransportationLocationSendDto;
import ru.logisticplatform.dto.transportation.TransportationTransportationLocationDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestMessage;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationLocation;
import ru.logisticplatform.model.transportation.TransportationStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.service.RestMessageService;
import ru.logisticplatform.service.transportation.TransportationLocationService;
import ru.logisticplatform.service.transportation.TransportationService;
import ru.logisticplatform.service.user.RoleService;
import ru.logisticplatform.service.user.UserService;

import java.awt.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transportations/locations/")
public class TransportationLocationRestControllerV1{

    private final TransportationLocationService transportationLocationService;
    private final TransportationService transportationService;
    private final UserService userService;
    private final RoleService roleService;
    private final RestMessageService restMessageService;

    @Autowired
    public TransportationLocationRestControllerV1(TransportationLocationService transportationLocationService
                                                    ,TransportationService transportationService
                                                    ,UserService userService
                                                    ,RoleService roleService
                                                    ,RestMessageService restMessageService){
        this.transportationLocationService = transportationLocationService;
        this.transportationService = transportationService;
        this.userService = userService;
        this.roleService = roleService;
        this.restMessageService = restMessageService;
    }

    /**
     *
     * @param authentication
     * @return
     */
    @GetMapping(value = "/me/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> myLocations(Authentication authentication){

        User user = this.userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.NOT_ACTIVE
                         || user.getUserStatus() == UserStatus.DELETED
                         || this.roleService.findUserRole(user, "ROLE_CUSTOMER")){

            RestMessage restMessage = this.restMessageService.findByCode("U003");
            RestMessageDto restMessageDto= ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<Transportation> transportations = transportationService.findAllByUserAndStatus(user, TransportationStatus.ACTIVE);

        if(transportations.isEmpty()){
            RestMessage restMessage = this.restMessageService.findByCode("T006");
            RestMessageDto restMessageDto= ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        TransportationLocation transportationLocation = transportationLocationService.findByTransportation(transportations.get(0));

        if(transportationLocation == null){

            RestMessage restMessage = this.restMessageService.findByCode("T007");
            RestMessageDto restMessageDto= ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        TransportationLocationDto transportationLocationDto = ObjectMapperUtils.map(transportationLocation, TransportationLocationDto.class);

        return new ResponseEntity<TransportationLocationDto>(transportationLocationDto, HttpStatus.OK);
        // TransportationLocation transportationLocation = transportationLocationService.findByTransportation()
    }


    /**
     *
     * @param authentication
     * @param transportationLocationSendDto
     * @return
     */
    @PostMapping(value = "/send/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerTransportaionLocation(Authentication authentication,
                                                         @RequestBody TransportationLocationSendDto transportationLocationSendDto){
        if (transportationLocationSendDto == null) {

            RestMessage restMessage = this.restMessageService.findByCode("S001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null
                || user.getUserStatus() == UserStatus.DELETED
                || user.getUserStatus() == UserStatus.NOT_ACTIVE
                || this.roleService.findUserRole(user, "ROLE_CUSTOMER")) {

            RestMessage restMessage = this.restMessageService.findByCode("U003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<Transportation> transportations = transportationService.findAllByUserAndStatus(user, TransportationStatus.ACTIVE);

        if(transportations.isEmpty()){
            RestMessage restMessage = this.restMessageService.findByCode("T006");
            RestMessageDto restMessageDto= ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        TransportationLocation transportationLocation = transportationLocationService.findByTransportation(transportations.get(0));

        if(transportationLocation == null){
            TransportationLocation transportationLocation1New = ObjectMapperUtils.map(transportationLocationSendDto, TransportationLocation.class);
            transportationLocation1New.setTransportation(transportations.get(0));
            TransportationLocation registeredTransportationLocation = transportationLocationService.save(transportationLocation1New);
            TransportationLocationDto transportationLocationDto = ObjectMapperUtils.map(registeredTransportationLocation
                                                                                        ,TransportationLocationDto.class);
            return new ResponseEntity<TransportationLocationDto>(transportationLocationDto, HttpStatus.OK);
        }

        transportationLocation.setLat(transportationLocationSendDto.getLat());
        transportationLocation.setLon(transportationLocationSendDto.getLon());
        transportationLocation.setUpdated(new Date());
        TransportationLocation registeredTransportationLocation = transportationLocationService.save(transportationLocation);
        TransportationLocationDto transportationLocationDto = ObjectMapperUtils.map(registeredTransportationLocation
                                                                                    ,TransportationLocationDto.class);
        return new ResponseEntity<TransportationLocationDto>(transportationLocationDto, HttpStatus.OK);
    }
}
