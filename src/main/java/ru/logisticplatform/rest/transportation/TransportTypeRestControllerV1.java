package ru.logisticplatform.rest.transportation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.logisticplatform.dto.RestErrorDto;
import ru.logisticplatform.dto.transportation.TransportTypeDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestError;
import ru.logisticplatform.model.transportation.TransportType;
import ru.logisticplatform.service.RestErrorService;
import ru.logisticplatform.service.transportation.TransportTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transporttypes/")
public class TransportTypeRestControllerV1 {

    private final TransportTypeService transportTypeService;
    private final RestErrorService restErrorService;

    @Autowired
    public TransportTypeRestControllerV1(TransportTypeService transportTypeService
                                            ,RestErrorService restErrorService){
        this.transportTypeService = transportTypeService;
        this.restErrorService = restErrorService;

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
            RestError restError = this.restErrorService.findByCode("T001");

            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);

            return new ResponseEntity<RestErrorDto>(restErrorDto, HttpStatus.NOT_FOUND);
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
            RestError restError = this.restErrorService.findByCode("T001");;

            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);

            return new ResponseEntity<RestErrorDto>(restErrorDto, HttpStatus.NOT_FOUND);
        }

        List<TransportTypeDto> transportTypesDto = ObjectMapperUtils.mapAll(transportType, TransportTypeDto.class);

        return new ResponseEntity<List<TransportTypeDto>>(transportTypesDto, HttpStatus.OK);
    }

}
