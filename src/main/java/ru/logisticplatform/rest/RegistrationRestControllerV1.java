package ru.logisticplatform.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.UserDto;
import ru.logisticplatform.model.User;
import ru.logisticplatform.service.UserService;

/**
 * REST controller for {@link User} registrated requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */


@RestController
@RequestMapping("/api/v1/registration/")
public class RegistrationRestControllerV1 {

    private final UserService userService;

    @Autowired
    public RegistrationRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto){
        HttpHeaders headers = new HttpHeaders();

        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(userDto.getUsername());

        if(user != null){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }

        this.userService.register(UserDto.toUser(userDto));

        return new ResponseEntity<>(userDto, headers, HttpStatus.CREATED);
    }

}
