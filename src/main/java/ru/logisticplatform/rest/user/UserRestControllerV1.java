package ru.logisticplatform.rest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.dto.user.UserDto;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.rest.user.admin.AdminRestControllerV1;
import ru.logisticplatform.service.user.UserService;

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

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long userId){

        if(userId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = this.userService.findById(userId);

        if (user == null || user.getUserStatus() == UserStatus.DELETED){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserDto userDto = ObjectMapperUtils.map(user, UserDto.class);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    /**
     *
     * @param userId
     * @return
     */

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long userId) {

        if(userId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = this.userService.findById(userId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.userService.updateUserStatus(user, UserStatus.DELETED);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
