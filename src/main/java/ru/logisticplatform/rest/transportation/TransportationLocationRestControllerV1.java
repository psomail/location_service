package ru.logisticplatform.rest.transportation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.transportation.TransportationLocationDto;
import ru.logisticplatform.model.transportation.TransportationLocation;
import ru.logisticplatform.service.transportation.TransportationLocationService;

import java.awt.*;

@RestController
@RequestMapping("/api/v1/transportations/locations/")
public class TransportationLocationRestControllerV1{

    private final TransportationLocationService transportationLocationService;

    @Autowired
    public TransportationLocationRestControllerV1(TransportationLocationService transportationLocationService){
        this.transportationLocationService = transportationLocationService;
    }

//    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public RequestMapping<TransportationLocationDto> getLocationByTransportation(@PathVariable("id") Long transportationId){
//
//        if(transportationId == null){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        TransportationLocation transportationLocation = this.transportationLocationService
//
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @DeleteMapping
//    public RequestMapping
}
