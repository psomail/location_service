package ru.logisticplatform.rest.deal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.logisticplatform.dto.RestMessageDto;
import ru.logisticplatform.dto.deal.CreateDealDto;
import ru.logisticplatform.dto.deal.DealDto;
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
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.service.RestMessageService;
import ru.logisticplatform.service.deal.DealService;
import ru.logisticplatform.service.order.OrderService;
import ru.logisticplatform.service.transportation.TransportationService;
import ru.logisticplatform.service.user.UserService;

import java.util.Calendar;
import java.util.Date;


@RestController
@RequestMapping("/api/v1/deals/")
public class DealRestControllerV1 {

    private final DealService dealService;
    private final OrderService orderService;
    private final TransportationService transportationService;
    private final RestMessageService restMessageService;
    private final UserService userService;

    @Autowired
    public DealRestControllerV1(DealService dealService
                                ,OrderService orderService
                                ,TransportationService transportationService
                                ,UserService userService
                                ,RestMessageService restMessageService){

        this.dealService = dealService;
        this.orderService = orderService;
        this.transportationService = transportationService;
        this.restMessageService = restMessageService;
        this.userService = userService;
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
}
