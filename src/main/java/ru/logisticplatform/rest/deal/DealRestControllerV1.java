package ru.logisticplatform.rest.deal;

import liquibase.pro.packaged.D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.logisticplatform.dto.RestMessageDto;
import ru.logisticplatform.dto.deal.CreateDealDto;
import ru.logisticplatform.dto.deal.DealDto;
import ru.logisticplatform.dto.deal.DealUserConfirmDto;
import ru.logisticplatform.dto.utils.ObjectMapperUtils;
import ru.logisticplatform.model.RestMessage;
import ru.logisticplatform.model.deal.Deal;
import ru.logisticplatform.model.deal.DealConfirmStatus;
import ru.logisticplatform.model.deal.DealStatus;
import ru.logisticplatform.model.goods.Goods;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.order.OrderStatus;
import ru.logisticplatform.model.transportation.Transportation;
import ru.logisticplatform.model.transportation.TransportationStatus;
import ru.logisticplatform.model.user.Role;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.service.RestMessageService;
import ru.logisticplatform.service.deal.DealService;
import ru.logisticplatform.service.order.OrderService;
import ru.logisticplatform.service.transportation.TransportationService;
import ru.logisticplatform.service.user.RoleService;
import ru.logisticplatform.service.user.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v1/deals/")
public class DealRestControllerV1 {

    private final DealService dealService;
    private final OrderService orderService;
    private final TransportationService transportationService;
    private final RestMessageService restMessageService;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DealRestControllerV1(DealService dealService
                                ,OrderService orderService
                                ,TransportationService transportationService
                                ,UserService userService
                                ,RoleService roleService
                                ,RestMessageService restMessageService){

        this.dealService = dealService;
        this.orderService = orderService;
        this.transportationService = transportationService;
        this.restMessageService = restMessageService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('CUSTOMER', 'CONTRACTOR')")
    public ResponseEntity<?> getDealById(@PathVariable("id") Long id, Authentication authentication){

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {
            RestMessage restMessage = restMessageService.findByCode("U002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Deal deal = dealService.findById(id);
        if(deal == null || deal.getDealStatus() == DealStatus.DELETED
                        || !user.equals(deal.getOrder().getUser())
                        || !user.equals(deal.getTransportation().getUser())){

            RestMessage restMessage = restMessageService.findByCode("D002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        DealDto dealDto = ObjectMapperUtils.map(deal, DealDto.class);

        return new ResponseEntity<DealDto>(dealDto, HttpStatus.OK);
    }

     /**
     *
     * @param authentication
     * @return
     */

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('CUSTOMER', 'CONTRACTOR')")
    public ResponseEntity<?> getAllDealByUser(Authentication authentication){

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {
            RestMessage restMessage = restMessageService.findByCode("U002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<Deal> deals = dealService.findAllByUserAndStatusNotLike(user, DealStatus.DELETED);

        if(deals == null || deals.isEmpty()){
            RestMessage restMessage = restMessageService.findByCode("D002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        List<DealDto> dealDtos = ObjectMapperUtils.mapAll(deals, DealDto.class);

        return new ResponseEntity<List<DealDto>>(dealDtos, HttpStatus.OK);
    }


    /**
     *
     * @param authentication
     * @param createDealDto
     * @return
     */
    @PostMapping(value = "/create/", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createDeal(Authentication authentication, @RequestBody CreateDealDto createDealDto){

        if (createDealDto == null) {
            RestMessage restMessage = this.restMessageService.findByCode("D003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED){
            RestMessage restMessage = this.restMessageService.findByCode("U001");
            RestMessageDto restMessageDto= ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Order order = orderService.findById(createDealDto.getOrder().getId());

        if(order == null || order.getOrderStatus() == OrderStatus.DELETED
                        || user.getId() != order.getUser().getId()){

            RestMessage restMessage = restMessageService.findByCode("Or003");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        if(order.getOrderStatus() != OrderStatus.ACTIVE){
            RestMessage restMessage = restMessageService.findByCode("Or005");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.MINUTE, +60);
        Date orderTimeMin = calendar.getTime();
        Date orderTime = order.getOrderDate();

        if(orderTime.before(orderTimeMin)){
            RestMessage restMessage = this.restMessageService.findByCode("Or002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Transportation transportation = transportationService.findById(createDealDto.getTransportation().getId());

        if(transportation == null || transportation.getTransportationStatus() == TransportationStatus.DELETED){
            RestMessage restMessage = restMessageService.findByCode("T002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        if(transportation.getTransportationStatus() != TransportationStatus.ACTIVE){
            RestMessage restMessage = restMessageService.findByCode("T006");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Deal deal = ObjectMapperUtils.map(createDealDto, Deal.class);
        deal.setOrder(order);
        deal.setTransportation(transportation);
        deal.setDealDate(orderTime);
        deal.setDealCustomerConfirm(DealConfirmStatus.YES);
        deal.setDealContractorConfirm(DealConfirmStatus.NO);
        deal.setDealStatus(DealStatus.CREATED);

        Deal createdDeal = dealService.createDeal(deal);

        DealDto dealDto = ObjectMapperUtils.map(createdDeal, DealDto.class);

        return new ResponseEntity<DealDto>(dealDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/confirm/", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('CUSTOMER', 'CONTRACTOR')")
    public ResponseEntity<?> setUserDealConfirm(Authentication authentication,
                                               @RequestBody DealUserConfirmDto dealUserConfirmDto){

        User user = userService.findByUsername(authentication.getName());

        if (user == null || user.getUserStatus() == UserStatus.DELETED) {
            RestMessage restMessage = restMessageService.findByCode("U002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Deal deal = dealService.findById(dealUserConfirmDto.getDealId());

        if (deal == null || deal.getDealStatus() == DealStatus.DELETED
                        || !user.equals(deal.getOrder().getUser())
                        || !user.equals(deal.getTransportation().getUser())){

            RestMessage restMessage = restMessageService.findByCode("D002");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        if (deal.getDealStatus() == DealStatus.CANCELLED_CUSTOMER){
            RestMessage restMessage = restMessageService.findByCode("D004");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.OK);
        }

        if (deal.getDealStatus() == DealStatus.CANCELLED_CONTRACTOR){
            RestMessage restMessage = restMessageService.findByCode("D005");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.OK);
        }

        if (deal.getDealStatus() == DealStatus.FINISHED
                && (deal.getDealContractorConfirm() == DealConfirmStatus.YES || deal.getDealCustomerConfirm() == DealConfirmStatus.YES)){

            RestMessage restMessage = restMessageService.findByCode("D007");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.OK);
        }

        if(roleService.findUserRole(user, "ROLE_CUSTOMER") && deal.getDealStatus() == DealStatus.FINISHED
                                                                    && deal.getDealCustomerConfirm() == DealConfirmStatus.NO ){

            deal.setDealCustomerConfirm(DealConfirmStatus.YES);
            dealService.updateDeal(deal);

            deal.getOrder().setOrderStatus(OrderStatus.NOT_ACTIVE);
            orderService.updateOrder(deal.getOrder());

            RestMessage restMessage = restMessageService.findByCode("D008");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.OK);
        }

        if(roleService.findUserRole(user, "ROLE_CONTRACTOR") && deal.getDealStatus() == DealStatus.FINISHED
                                                                      && deal.getDealContractorConfirm() == DealConfirmStatus.NO ){

            deal.setDealContractorConfirm(DealConfirmStatus.YES);
            dealService.updateDeal(deal);

            deal.getTransportation().setTransportationStatus(TransportationStatus.NOT_ACTIVE);
            transportationService.updateTransportation(deal.getTransportation());

            RestMessage restMessage = restMessageService.findByCode("D008");
            RestMessageDto restMessageDto = ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.OK);
        }

// доделать смену статуса сделки со всеми зависимостями
        return null;
    }

}
