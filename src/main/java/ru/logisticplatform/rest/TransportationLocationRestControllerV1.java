package ru.logisticplatform.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.RestMessageDto;
import ru.logisticplatform.dto.transportation.*;
import ru.logisticplatform.dto.transportation.location.*;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestMessage;
import ru.logisticplatform.model.transportation.TransportType;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationLocation;
import ru.logisticplatform.model.transportation.TransportationStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.service.RestMessageService;
import ru.logisticplatform.service.transportation.TransportTypeService;
import ru.logisticplatform.service.transportation.TransportationLocationService;
import ru.logisticplatform.service.transportation.TransportationService;
import ru.logisticplatform.service.user.RoleService;
import ru.logisticplatform.service.user.UserService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transportations/locations/")
public class TransportationLocationRestControllerV1{

    private final TransportationLocationService transportationLocationService;
    private final TransportationService transportationService;
    private final TransportTypeService transportTypeService;
    private final UserService userService;
    private final RoleService roleService;
    private final RestMessageService restMessageService;

    @Autowired
    public TransportationLocationRestControllerV1(TransportationLocationService transportationLocationService
                                                    ,TransportationService transportationService
                                                    ,TransportTypeService transportTypeService
                                                    ,UserService userService
                                                    ,RoleService roleService
                                                    ,RestMessageService restMessageService){
        this.transportationLocationService = transportationLocationService;
        this.transportationService = transportationService;
        this.transportTypeService = transportTypeService;
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
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<?> myLocations(Authentication authentication){

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.NOT_ACTIVE
                         || user.getUserStatus() == UserStatus.DELETED){
            RestMessage restMessage = restMessageService.findByCode("U003");
            RestMessageDto restMessageDto= ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<Transportation> transportations = transportationService.findAllByUserAndStatus(user, TransportationStatus.ACTIVE);

        if(transportations.isEmpty()){
            RestMessage restMessage = restMessageService.findByCode("T006");
            RestMessageDto restMessageDto= ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        TransportationLocation transportationLocation = transportationLocationService.findByTransportation(transportations.get(0));

        if(transportationLocation == null){

            RestMessage restMessage = restMessageService.findByCode("T007");
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
    @PreAuthorize("hasRole('CONTRACTOR')")
    public ResponseEntity<?> registerTransportaionLocation(Authentication authentication,
                                                         @RequestBody TransportationLocationSendDto transportationLocationSendDto){
        if (transportationLocationSendDto == null) {

            RestMessage restMessage = restMessageService.findByCode("S001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null
                || user.getUserStatus() == UserStatus.DELETED
                || user.getUserStatus() == UserStatus.NOT_ACTIVE) {

            RestMessage restMessage = restMessageService.findByCode("U003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<Transportation> transportations = transportationService.findAllByUserAndStatus(user, TransportationStatus.ACTIVE);

        if(transportations.isEmpty()){
            RestMessage restMessage = restMessageService.findByCode("T006");
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

    /**
     *
     * @param authentication
     * @param transportationLocationTransportTypesDto
     * @return
     */
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> contractorLocationsByTransportTypes(Authentication authentication,
                                         @RequestBody TransportationLocationTransportTypesDto transportationLocationTransportTypesDto){

        User user = this.userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.NOT_ACTIVE || user.getUserStatus() == UserStatus.DELETED){
            RestMessage restMessage = restMessageService.findByCode("U001");
            RestMessageDto restMessageDto= ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Double lonFrom = transportationLocationTransportTypesDto.getLon() - transportationLocationTransportTypesDto.getLength()/2;
        Double lonTo = transportationLocationTransportTypesDto.getLon() + transportationLocationTransportTypesDto.getLength()/2;
        Double latFrom = transportationLocationTransportTypesDto.getLat() - transportationLocationTransportTypesDto.getWidth()/2;
        Double latTo = transportationLocationTransportTypesDto.getLat() + transportationLocationTransportTypesDto.getWidth()/2;

        List<TransportTypeCreateTransportationDto> transportTypes = transportationLocationTransportTypesDto.getTransportTypes();
        List<Long> ids = transportTypes.stream().map(TransportTypeCreateTransportationDto::getId)
                                                .collect(Collectors.toList());

        if(transportTypes.isEmpty()){
            ids = transportTypeService.getAll().stream().map(TransportType::getId)
                                                        .collect(Collectors.toList());
        }

        List<TransportationLocation> transportationLocations = transportationLocationService.findAllByTransportTypes(lonFrom
                                                                                                                    ,lonTo
                                                                                                                    ,latFrom
                                                                                                                    ,latTo
                                                                                                                    ,ids);
        if(transportationLocations.isEmpty()){
            RestMessage restMessage = restMessageService.findByCode("T009");
            RestMessageDto restMessageDto= ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<TransportationLocationToCustomerDto> transportationLocationDto =
                            ObjectMapperUtils.mapAll(transportationLocations, TransportationLocationToCustomerDto.class);

        return new ResponseEntity<List<TransportationLocationToCustomerDto>>(transportationLocationDto, HttpStatus.OK);
    }

}
