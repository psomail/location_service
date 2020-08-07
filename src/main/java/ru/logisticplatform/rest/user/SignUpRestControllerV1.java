package ru.logisticplatform.rest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.user.SignUpUserDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.service.user.RoleService;
import ru.logisticplatform.service.user.UserService;
import ru.logisticplatform.service.user.UserTypeService;

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
    public ResponseEntity<SignUpUserDto> saveUser(@RequestBody SignUpUserDto userDto){
        HttpHeaders headers = new HttpHeaders();

        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(this.userService.findByUsername(userDto.getUsername()) != null){
            return new ResponseEntity<>(HttpStatus.FOUND);
        }

        userDto.getRoles().forEach(role -> role.setId(roleService.findByRoleName(role.getName()).getId()));
        userDto.getUserTypes().forEach(userType -> userType.setId(userTypeService.findByUserTypeName(userType.getName()).getId()));

        this.userService.signUp(ObjectMapperUtils.map(userDto, User.class));

        return new ResponseEntity<>(userDto, headers, HttpStatus.CREATED);
    }

}
