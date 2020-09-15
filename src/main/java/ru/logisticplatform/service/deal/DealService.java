package ru.logisticplatform.service.deal;

import ru.logisticplatform.model.deal.Deal;
import ru.logisticplatform.model.deal.DealStatus;
import ru.logisticplatform.model.user.User;

import java.util.List;

public interface DealService {

    Deal findById(Long id);

    List<Deal> findAllByUserAndStatusNotLike(User user, DealStatus dealStatus);

    Deal createDeal(Deal deal);
    Deal updateDeal(Deal deal);
}
