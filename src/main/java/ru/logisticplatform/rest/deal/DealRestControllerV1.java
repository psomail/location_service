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
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.service.RestMessageService;
import ru.logisticplatform.service.deal.DealService;
import ru.logisticplatform.service.user.UserService;


@RestController
@RequestMapping("/api/v1/deals/")
public class DealRestControllerV1 {

    private final DealService dealService;
    private final RestMessageService restMessageService;
    private final UserService userService;

    @Autowired
    public DealRestControllerV1(DealService dealService
                                ,UserService userService
                                ,RestMessageService restMessageService){

        this.dealService = dealService;
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

        if (user == null || user.getUserStatus() == UserStatus.NOT_ACTIVE || user.getUserStatus() == UserStatus.DELETED){
            RestMessage restMessage = this.restMessageService.findByCode("U001");
            RestMessageDto restMessageDto= ObjectMapperUtils.map(restMessage, RestMessageDto.class);
            return new ResponseEntity<RestMessageDto>(restMessageDto, HttpStatus.NOT_FOUND);
        }

        Deal deal = ObjectMapperUtils.map(createDealDto, Deal.class);

        dealService.createDeal(deal);

        DealDto dealDto = ObjectMapperUtils.map(deal, DealDto.class);

        return new ResponseEntity<DealDto>(dealDto, HttpStatus.CREATED);
    }

}
