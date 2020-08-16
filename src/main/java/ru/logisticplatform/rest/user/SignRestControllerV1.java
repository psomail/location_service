package ru.logisticplatform.rest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.user.AuthenticationRequestDto;
import ru.logisticplatform.dto.user.SignUpUserDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.security.jwt.JwtTokenProvider;
import ru.logisticplatform.service.user.RoleService;
import ru.logisticplatform.service.user.UserService;
import ru.logisticplatform.service.user.UserTypeService;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for {@link User} registrated requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */


@RestController
@RequestMapping("/api/v1/sign/")
public class SignRestControllerV1 {

    private final UserService userService;
    private final RoleService roleService;
    private final UserTypeService userTypeService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SignRestControllerV1(UserService userService, RoleService roleService, UserTypeService userTypeService,
                                AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {

        this.userService = userService;
        this.roleService = roleService;
        this.userTypeService = userTypeService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "up", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();

            String password = requestDto.getPassword();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(username, password);

            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}
