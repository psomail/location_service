package ru.logisticplatform.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.UserDto;
import ru.logisticplatform.model.Status;
import ru.logisticplatform.model.User;
import ru.logisticplatform.service.UserService;
import java.util.List;

/**
 * REST controller for {@link User} connected requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@RestController
@RequestMapping("/api/v1/users/")
public class UserRestControllerV1 {

    private final UserService userService;
    private final AdminRestControllerV1 adminRestControllerV1;

    @Autowired
    public UserRestControllerV1(UserService userService, AdminRestControllerV1 adminRestControllerV1) {
        this.userService = userService;
        this.adminRestControllerV1 = adminRestControllerV1;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long userId){

//        UserDto userDto = new UserDto();
//
//    if(userId == null){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        User user = this.userService.findById(userId);
//
//        if (user == null){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
        return adminRestControllerV1.getUser(userId);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long userId) {

        if(userId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = this.userService.findById(userId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.userService.updateUserStatus(user, Status.DELETED);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
