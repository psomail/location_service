package ru.logisticplatform.rest;


import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.logisticplatform.dto.AdminUserDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.dto.UserDto;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.service.UserService;

import java.util.List;

/**
 * REST controller for Admin {@link User} connected requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@RestController
@RequestMapping("/api/v1/admins/")
public class AdminRestControllerV1 {

    private final UserService userService;

    @Autowired
    public AdminRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdminUserDto>> getAllUser(){
        List<User>  users = this.userService.getAll();

        if(users.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<AdminUserDto> adminUsersDto = ObjectMapperUtils.mapAll(users, AdminUserDto.class);

        return new ResponseEntity<>(adminUsersDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminUserDto> getUser(@PathVariable("id") Long userId){

        if(userId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = this.userService.findById(userId);

        if (user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AdminUserDto adminUserDto = ObjectMapperUtils.map(user, AdminUserDto.class);

        return new ResponseEntity<>(adminUserDto, HttpStatus.OK);
    }


    @RequestMapping(value = "/users/status/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdminUserDto>> getUser(@PathVariable("status") UserStatus userStatus){

        if(userStatus == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<User> users = this.userService.findByStatus(userStatus);

        if(users.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<AdminUserDto> adminUsersDto = ObjectMapperUtils.mapAll(users, AdminUserDto.class);

        return new ResponseEntity<>(adminUsersDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}/status/{status}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUserStatus(@PathVariable("id") Long userId, @PathVariable("status") String userStatus) {

        if(userId == null || userStatus == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = this.userService.findById(userId);
        UserStatus status = EnumUtils.getEnumIgnoreCase(UserStatus.class, userStatus);

        if (user == null || status == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AdminUserDto adminUserDto = ObjectMapperUtils.map(this.userService.updateUserStatus(user, status), AdminUserDto.class);

        return new ResponseEntity<>(adminUserDto, HttpStatus.OK);
    }



//    @RequestMapping(value = "/type/{tyoe}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<UserDto>> getUser(@PathVariable("userStatus") String type){
//
//        if(userStatus == null){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        List<User> users = this.userService.findByStatus(userStatus);
//
//        if(users.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(UserDto.fromUser(users), HttpStatus.OK);
//    }
}
