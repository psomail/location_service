package ru.logisticplatform.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
