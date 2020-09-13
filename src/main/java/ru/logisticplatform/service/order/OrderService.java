package ru.logisticplatform.service.order;

import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.order.OrderStatus;
import ru.logisticplatform.model.user.User;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    Order updateOrder(Order order);

    Order findById(Long id);

    List<Order> findAllByUserAndStatusNotLike(User user, OrderStatus status);

}
