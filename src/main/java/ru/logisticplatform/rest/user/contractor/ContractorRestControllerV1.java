package ru.logisticplatform.rest.user.contractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.RestErrorDto;
import ru.logisticplatform.dto.user.UserDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestError;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.service.RestErrorService;
import ru.logisticplatform.service.user.RoleService;
import ru.logisticplatform.service.user.UserService;

/**
 * REST controller for {@link User} connected requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@RestController
@RequestMapping("/api/v1/contractors/")
public class ContractorRestControllerV1 {

    private final UserService userService;
    private final RoleService roleService;
    private final RestErrorService restErrorService;

    @Autowired
    public ContractorRestControllerV1(UserService userService
                                        ,RoleService roleService
                                        ,RestErrorService restErrorService) {
        this.userService = userService;
        this.roleService = roleService;
        this.restErrorService = restErrorService;
    }

    /**
     *
     * @param authentication
     * @return
     */

    @GetMapping(value = "me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMe(Authentication authentication/*, Principal principal*/) {
        //System.out.println(principal.getName());

        User user = this.userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED){

            RestError restError = this.restErrorService.findByCode("U002");

            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);

            return new ResponseEntity<RestErrorDto>(restErrorDto, HttpStatus.NOT_FOUND);
        }

        UserDto userDto = ObjectMapperUtils.map(user, UserDto.class);

        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    /**
     *
     * @param userName
     * @return
     */

    @GetMapping(value = "/customers/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCustomer(@PathVariable("name") String userName){

        if(userName == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = this.userService.findByUsername(userName);

        if (user == null || user.getUserStatus() != UserStatus.ACTIVE
                || this.roleService.findUserRole(user, "ROLE_CONTRACTOR")
                || this.roleService.findUserRole(user, "ROLE_ADMIN")){

            RestError restError = this.restErrorService.findByCode("U001");

            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);

            return new ResponseEntity<RestErrorDto>(restErrorDto, HttpStatus.NOT_FOUND);
        }

        UserDto userDto = ObjectMapperUtils.map(user, UserDto.class);

        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    /**
     *
     * @param authentication
     * @return
     */

    @DeleteMapping(value = "me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMe(Authentication authentication/*, Principal principal*/) {

        //System.out.println(principal.getName());

        User user = this.userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {

            RestError restError = this.restErrorService.findByCode("U002");

            RestErrorDto restErrorDto= ObjectMapperUtils.map(restError, RestErrorDto.class);

            return new ResponseEntity<RestErrorDto>(restErrorDto, HttpStatus.NOT_FOUND);
        }

        this.userService.updateUserStatus(user, UserStatus.DELETED);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
