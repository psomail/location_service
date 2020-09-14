package ru.logisticplatform.service.deal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.deal.Deal;
import ru.logisticplatform.repository.deal.DealRepository;
import ru.logisticplatform.service.deal.DealService;


@Service
@Slf4j
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;

    @Autowired
    public DealServiceImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
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
}
