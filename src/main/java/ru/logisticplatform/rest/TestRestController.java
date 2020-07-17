package ru.logisticplatform.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.logisticplatform.dto.UserDto;
import ru.logisticplatform.model.Status;
import ru.logisticplatform.model.User;
import ru.logisticplatform.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tests/")
public class TestRestController {

    private final UserService userService;

    @Autowired
    public TestRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/status/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getUser(@PathVariable("status") Status status){

        if(status == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<User> users = this.userService.findByStatus(status);

        if(users.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(UserDto.fromUser(users), HttpStatus.OK);
    }
}
