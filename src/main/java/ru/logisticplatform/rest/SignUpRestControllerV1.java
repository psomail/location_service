package ru.logisticplatform.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.UserDto;
import ru.logisticplatform.model.User;
import ru.logisticplatform.service.RoleService;
import ru.logisticplatform.service.UserService;
import ru.logisticplatform.service.UserTypeService;

/**
 * REST controller for {@link User} registrated requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */


@RestController
@RequestMapping("/api/v1/signup/")
public class SignUpRestControllerV1 {

    private final UserService userService;
    private final RoleService roleService;
    private final UserTypeService userTypeService;

    @Autowired
    public SignUpRestControllerV1(UserService userService, RoleService roleService, UserTypeService userTypeService) {

        this.userService = userService;
        this.roleService = roleService;
        this.userTypeService = userTypeService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto){
        HttpHeaders headers = new HttpHeaders();

        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(this.userService.findByUsername(userDto.getUsername()) != null){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }

        userDto.getRoles().forEach(role -> role.setId(roleService.findByRoleName(role.getName()).getId()));
        userDto.getUserTypes().forEach(userType -> userType.setId(userTypeService.findByUserTypeName(userType.getName()).getId()));

        this.userService.signUp(UserDto.toUser(userDto));

        return new ResponseEntity<>(userDto, headers, HttpStatus.CREATED);
    }

}
