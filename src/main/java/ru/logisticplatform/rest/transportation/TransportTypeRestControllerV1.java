package ru.logisticplatform.rest.transportation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.logisticplatform.dto.RestMessageDto;
import ru.logisticplatform.dto.transportation.TransportTypeDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestMessage;
import ru.logisticplatform.model.transportation.TransportType;
import ru.logisticplatform.service.RestMessageService;
import ru.logisticplatform.service.transportation.TransportTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transporttypes/")
public class TransportTypeRestControllerV1 {

    private final TransportTypeService transportTypeService;
    private final RestMessageService restMessageService;

    @Autowired
    public TransportTypeRestControllerV1(TransportTypeService transportTypeService
                                            , RestMessageService restMessageService){
        this.transportTypeService = transportTypeService;
        this.restMessageService = restMessageService;

    }

    /**
     *
     * @param transportTypeId
     * @return
     */

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransportTypeById(@PathVariable("id") Long transportTypeId){

//        if(transportTypeId == null){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }

        TransportType transportType = this.transportTypeService.findById(transportTypeId);

        if (transportType == null){
            RestMessage restMessage = this.restMessageService.findByCode("T001");

            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        TransportTypeDto transportTypeDto = ObjectMapperUtils.map(transportType, TransportTypeDto.class);

        return new ResponseEntity<TransportTypeDto>(transportTypeDto, HttpStatus.OK);
    }


    /**
     *
     * @return
     */

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTransportType(){

        List<TransportType> transportType = this.transportTypeService.getAll();

        if(transportType.isEmpty()){
            RestMessage restMessage = this.restMessageService.findByCode("T001");;

            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);

            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<TransportTypeDto> transportTypesDto = ObjectMapperUtils.mapAll(transportType, TransportTypeDto.class);

        return new ResponseEntity<List<TransportTypeDto>>(transportTypesDto, HttpStatus.OK);
    }

}
