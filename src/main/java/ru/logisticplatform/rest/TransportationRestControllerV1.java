package ru.logisticplatform.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.logisticplatform.dto.TransportTypeDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.order.TransportType;
import ru.logisticplatform.model.order.Transportation;
import ru.logisticplatform.service.TransportTypeService;


/**
 * REST controller for {@link Transportation} connected requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@RestController
@RequestMapping("/api/v1/transportations/")
public class TransportationRestControllerV1 {

    private final TransportTypeService transportTypeService;

    @Autowired
    public TransportationRestControllerV1(TransportTypeService transportTypeService){
        this.transportTypeService = transportTypeService;
    }

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
}
