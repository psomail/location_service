package ru.logisticplatform.repository.deal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.deal.Deal;
import ru.logisticplatform.model.deal.DealStatus;
import ru.logisticplatform.model.user.User;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal, Long> {

    List<Deal> findAllByOrder_UserAndDealStatusNotLike(User user, DealStatus dealStatus);
    List<Deal> findAllByTransportation_UserAndDealStatusNotLike(User user, DealStatus dealStatus);
}
