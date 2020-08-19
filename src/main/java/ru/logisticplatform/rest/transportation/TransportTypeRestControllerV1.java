package ru.logisticplatform.rest.transportation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.logisticplatform.dto.transportation.TransportTypeDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.transportation.TransportType;
import ru.logisticplatform.service.transportation.TransportTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transporttypes/")
public class TransportTypeRestControllerV1 {

    private final TransportTypeService transportTypeService;

    @Autowired
    public TransportTypeRestControllerV1(TransportTypeService transportTypeService){
        this.transportTypeService = transportTypeService;
    }

    /**
     *
     * @param transportTypeId
     * @return
     */

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransportTypeDto> getTransportTypeById(@PathVariable("id") Long transportTypeId){

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
     * @return
     */

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransportTypeDto>> getAllTransportType(){

        List<TransportType> transportType = this.transportTypeService.getAll();

        if(transportType.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<TransportTypeDto> transportTypesDto = ObjectMapperUtils.mapAll(transportType, TransportTypeDto.class);

        return new ResponseEntity<>(transportTypesDto, HttpStatus.OK);
    }

}
