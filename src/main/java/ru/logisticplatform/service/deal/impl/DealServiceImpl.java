package ru.logisticplatform.service.deal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.deal.Deal;
import ru.logisticplatform.model.deal.DealStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.model.user.UserStatus;
import ru.logisticplatform.repository.deal.DealRepository;
import ru.logisticplatform.service.deal.DealService;
import ru.logisticplatform.service.user.RoleService;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final RoleService roleService;

    @Autowired
    public DealServiceImpl(DealRepository dealRepository
                            ,RoleService roleService) {
        this.dealRepository = dealRepository;
        this.roleService = roleService;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Deal findById(Long id) {

        Deal deal = dealRepository.findById(id).orElse(null);

        if(deal == null){
            log.warn("IN DealServiceImpl findById() - no deal found by id: {}", id);
         }

        return deal;
    }

    /**
     *
     * @param user
     * @param dealStatus
     * @return
     */
    @Override
    public List<Deal> findAllByUserAndStatusNotLike(User user, DealStatus dealStatus) {

        if(roleService.findUserRole(user, "ROLE_CUSTOMER")){
           List<Deal> dealsCustomer = dealRepository.findAllByOrder_UserAndDealStatusNotLike(user, DealStatus.DELETED);

           if(dealsCustomer.isEmpty()){
               log.warn("IN DealServiceImpl findAllByUserAndStatusNotLike() - no deals found by user customer: {}", user.getId());
           }
           return dealsCustomer;
        }

        if(roleService.findUserRole(user, "ROLE_CONTRACTOR")){
            List<Deal> dealsContractor = dealRepository.findAllByTransportation_UserAndDealStatusNotLike(user, DealStatus.DELETED);

            if(dealsContractor.isEmpty()) {
                log.warn("IN DealServiceImpl findAllByUserAndStatusNotLike() - no deals found by user contractor: {}", user.getId());
            }
            return dealsContractor;
        }
        return null;
    }

    /**
     *
     * @param deal
     * @return
     */
    @Override
    public Deal createDeal(Deal deal){

        Deal createdDeal = dealRepository.save(deal);

        log.info("IN DealServiceImpl createDeal() - deal ID: {} successfully created", createdDeal.getId());

    return createdDeal;
    }

    @Override
    public Deal updateDeal(Deal deal){

        Deal updatedDeal = dealRepository.save(deal);

        log.info("IN DealServiceImpl updateDeal() - deal ID: {} successfully updated", updatedDeal.getId());

        return updatedDeal;
    }
}
