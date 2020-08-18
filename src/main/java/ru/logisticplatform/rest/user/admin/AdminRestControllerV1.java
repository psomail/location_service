package ru.logisticplatform.rest.user.admin;


import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.goods.admin.GoodsAdminDto;
import ru.logisticplatform.dto.user.admin.AdminUserDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.dto.user.UserDto;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.service.goods.GoodsService;
import ru.logisticplatform.service.user.UserService;

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
    private final GoodsService goodsService;

    @Autowired
    public AdminRestControllerV1(UserService userService, GoodsService goodsService) {
        this.userService = userService;
        this.goodsService = goodsService;
    }


    /**
     *
     * @return
     */

    @GetMapping(value = "/users/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdminUserDto>> getAllUsers(){
        List<User>  users = this.userService.getAll();

        if(users.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<AdminUserDto> adminUsersDto = ObjectMapperUtils.mapAll(users, AdminUserDto.class);

        return new ResponseEntity<>(adminUsersDto, HttpStatus.OK);
    }

    /**
     *
     * @param userId
     * @return
     */

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable("id") Long userId){

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

    /**
     *
     * @param userId
     * @return
     */


    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long userId) {

        if(userId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = this.userService.findById(userId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.userService.delete(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     *
     * @param userStatus
     * @return
     */

    @GetMapping(value = "/users/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdminUserDto>> getUsersByStatus(@PathVariable("status") String userStatus){

        if(userStatus == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<User> users = this.userService.findByStatus(EnumUtils.getEnumIgnoreCase(UserStatus.class, userStatus));

        if(users.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<AdminUserDto> adminUsersDto = ObjectMapperUtils.mapAll(users, AdminUserDto.class);

        return new ResponseEntity<>(adminUsersDto, HttpStatus.OK);
    }


    /**
     *
     * @param userId
     * @param userStatus
     * @return
     */

    @PutMapping(value = "/users/{id}/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
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


    /**
     *
     * @param userId
     * @return
     */

    @GetMapping(value = "/users/{id}/goods/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GoodsAdminDto>> getAllGoodsByUserId(@PathVariable("id") Long userId){

        if(userId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user =  userService.findById(userId);

        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Goods> goods = goodsService.findAllByUser(user);

        List<GoodsAdminDto> goodsAdminDtos = ObjectMapperUtils.mapAll(goods, GoodsAdminDto.class);

        return new ResponseEntity<>(goodsAdminDtos, HttpStatus.OK);
    }



//    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
