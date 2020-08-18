package ru.logisticplatform.rest.user.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.dto.user.UserDto;
import ru.logisticplatform.model.user.Role;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.rest.user.admin.AdminRestControllerV1;
import ru.logisticplatform.service.user.RoleService;
import ru.logisticplatform.service.user.UserService;

import java.security.Principal;
import java.util.List;

/**
 * REST controller for {@link User} connected requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@RestController
@RequestMapping("/api/v1/customers/")
public class CustomerRestControllerV1 {

    private final UserService userService;
    private final RoleService roleService;
    private final AdminRestControllerV1 adminRestControllerV1;

    @Autowired
    public CustomerRestControllerV1(     UserService userService
                                        ,RoleService roleService
                                        ,AdminRestControllerV1 adminRestControllerV1) {
        this.userService = userService;
        this.roleService = roleService;
        this.adminRestControllerV1 = adminRestControllerV1;
    }

    /**
     *
     * @param authentication
     * @return
     */

    @GetMapping(value = "me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getMe(Authentication authentication/*, Principal principal*/) {
        //System.out.println(principal.getName());

        User user = this.userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserDto userDto = ObjectMapperUtils.map(user, UserDto.class);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     *
     * @param userName
     * @return
     */

    @GetMapping(value = "/contractors/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getContractor(@PathVariable("name") String userName){

        if(userName == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = this.userService.findByUsername(userName);

        if (user == null || user.getUserStatus() != UserStatus.ACTIVE
                || this.roleService.findUserRole(user, "ROLE_CUSTOMER")
                || this.roleService.findUserRole(user, "ROLE_ADMIN")){
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
