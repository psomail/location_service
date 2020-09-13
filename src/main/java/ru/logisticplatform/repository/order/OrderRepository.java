package ru.logisticplatform.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.order.OrderStatus;
import ru.logisticplatform.model.user.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserAndOrderStatusNotLike(User user, OrderStatus orderStatusNotLike);
}
