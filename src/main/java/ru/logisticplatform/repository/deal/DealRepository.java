package ru.logisticplatform.repository.deal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.deal.Deal;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
