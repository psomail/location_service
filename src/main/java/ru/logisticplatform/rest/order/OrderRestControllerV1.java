package ru.logisticplatform.rest.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import ru.logisticplatform.dto.RestMessageDto;
import ru.logisticplatform.dto.goods.GoodsForCreateOrderDto;
import ru.logisticplatform.dto.order.CreateOrderDto;
import ru.logisticplatform.dto.order.OrderDto;
import ru.logisticplatform.dto.order.UpdateOrderDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestMessage;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.goods.GoodsStatus;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.order.OrderStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.service.RestMessageService;
import ru.logisticplatform.service.goods.GoodsService;
import ru.logisticplatform.service.order.OrderService;
import ru.logisticplatform.service.user.UserService;

import javax.xml.crypto.Data;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * REST controller for {@link Order} connected requests.
 *
 * @author Sergei Perminov
 * @version 1.0
 */

@RestController
@RequestMapping("/api/v1/orders/")
public class OrderRestControllerV1 {

    private final OrderService orderService;
    private final GoodsService goodsService;
    private final UserService userService;
    private final RestMessageService restMessageService;

    @Autowired
    public OrderRestControllerV1(OrderService orderService
                                ,GoodsService goodsService
                                ,UserService userService
                                ,RestMessageService restMessageService) {
        this.orderService = orderService;
        this.goodsService = goodsService;
        this.userService = userService;
        this.restMessageService = restMessageService;
    }

    /**
     *
     * @param orderId
     * @param authentication
     * @return
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long orderId,  Authentication authentication){

        User user = this.userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {
            RestMessage restMessage = restMessageService.findByCode("U001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Order order = orderService.findById(orderId);

        if(order == null || order.getOrderStatus() == OrderStatus.DELETED
                         || !user.equals(order.getUser())){

            RestMessage restMessage = restMessageService.findByCode("Or003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        OrderDto orderDto = ObjectMapperUtils.map(order, OrderDto.class);

        return new ResponseEntity<OrderDto>(orderDto, HttpStatus.OK);
    }


  /**
   *
   * @param authentication
   * @return
   */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getTransportaionsByUser(Authentication authentication){

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {
            RestMessage restMessage = restMessageService.findByCode("U001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<Order> orders = orderService.findAllByUserAndStatusNotLike(user, OrderStatus.DELETED);

        if(orders == null || orders.isEmpty()){
            RestMessage restMessage = restMessageService.findByCode("Or003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<OrderDto> orderDtos = ObjectMapperUtils.mapAll(orders, OrderDto.class);

        return new ResponseEntity<List<OrderDto>>(orderDtos, HttpStatus.OK);
    }


    /**
     *
     * @param authentication
     * @param createOrderDto
     * @return
     */

    @PostMapping(value = "/create/", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createOrder(Authentication authentication, @RequestBody CreateOrderDto createOrderDto){

        if(createOrderDto == null){
            RestMessage restMessage = restMessageService.findByCode("G003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {
            RestMessage restMessage = restMessageService.findByCode("U001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<Long> ids = createOrderDto.getGoods().stream()
                                                  .map(GoodsForCreateOrderDto::getId)
                                                  .collect(Collectors.toList());
        List<Goods> goods = goodsService.findAllByUserAndIdIn(user, ids);

        if(goods.isEmpty()){
            RestMessage restMessage = this.restMessageService.findByCode("G002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.MINUTE, +30);
        Date orderTimeMin = calendar.getTime();
        Date orderTime = createOrderDto.getOrderDate();

        if(orderTime.before(orderTimeMin)){
            RestMessage restMessage = this.restMessageService.findByCode("Or002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Order order = ObjectMapperUtils.map(createOrderDto, Order.class);
        order.setUser(user);
        order.setGoods(goods);
        order.setOrderDate(orderTime);
        orderService.createOrder(order);

        OrderDto orderDto = ObjectMapperUtils.map(order, OrderDto.class);

        return new ResponseEntity<OrderDto>(orderDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateOrder(Authentication authentication, @RequestBody UpdateOrderDto updateOrderDto){

        if(updateOrderDto == null){
            RestMessage restMessage = restMessageService.findByCode("S001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {
            RestMessage restMessage = restMessageService.findByCode("U001");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Order order = orderService.findById(updateOrderDto.getId());

        if(order == null || order.getOrderStatus() == OrderStatus.DELETED
                         || user.getId() != order.getUser().getId()){

            RestMessage restMessage = restMessageService.findByCode("Or003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<Long> ids = updateOrderDto.getGoods().stream()
                                                        .map(GoodsForCreateOrderDto::getId)
                                                        .collect(Collectors.toList());
        List<Goods> goods = goodsService.findAllByUserAndIdIn(user, ids);

        if (goods.isEmpty()){
            RestMessage restMessage = this.restMessageService.findByCode("G002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.MINUTE, +30);
        Date orderTimeMin = calendar.getTime();
        Date orderTime = updateOrderDto.getOrderDate();

        if(orderTime.before(orderTimeMin)){
            RestMessage restMessage = this.restMessageService.findByCode("Or002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Order updatingOrder = ObjectMapperUtils.map(updateOrderDto, Order.class);
        updatingOrder.setUser(user);
        updatingOrder.setGoods(goods);
        updatingOrder.setOrderStatus(order.getOrderStatus());
        updatingOrder.setCreated(order.getCreated());

        orderService.updateOrder(updatingOrder);

        OrderDto orderDto = ObjectMapperUtils.map(updatingOrder, OrderDto.class);

        return new ResponseEntity<OrderDto>(orderDto, HttpStatus.OK);
    }
}
