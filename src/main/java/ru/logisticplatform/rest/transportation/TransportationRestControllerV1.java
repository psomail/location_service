package ru.logisticplatform.rest.transportation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.transportation.TransportTypeDto;
import ru.logisticplatform.dto.transportation.TransportationDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.transportation.TransportType;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationStatus;
import ru.logisticplatform.service.transportation.TransportTypeService;
import ru.logisticplatform.service.transportation.TransportationService;

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

    @Autowired
    public TransportationRestControllerV1(TransportationService transportationService, TransportTypeService transportTypeService){
        this.transportationService = transportationService;
        this.transportTypeService = transportTypeService;
    }


    /**
     *
     * @param transportTypeId
     * @return
     */

    @GetMapping(value = "/transporttypes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransportTypeDto> geTransportType(@PathVariable("id") Long transportTypeId){

        if(transportTypeId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TransportType transportType = this.transportTypeService.findById(transportTypeId);

        if (transportType == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TransportTypeDto transportTypeDto = ObjectMapperUtils.map(transportType, TransportTypeDto.class);

        return new ResponseEntity<>(transportTypeDto, HttpStatus.OK);
    }

    /**
     * 
     * @param transportationId
     * @return
     */

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransportationDto> getTransportaionById(@PathVariable("id") Long transportationId){

        if (transportationId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Transportation transportation = this.transportationService.findById(transportationId);

        if(transportation == null || transportation.getTransportationStatus() == TransportationStatus.DELETED){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TransportationDto transportationDto = ObjectMapperUtils.map(transportation, TransportationDto.class);

        return new ResponseEntity<>(transportationDto, HttpStatus.OK);
    }


    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Goods> deleteTransportaion(@PathVariable("id") Long transportTypeId){

        if(transportTypeId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Transportation transportation = this.transportationService.findById(transportTypeId);

        if (transportation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        transportation.setTransportationStatus(TransportationStatus.DELETED);

        this.transportationService.updateTransportation(transportation);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
