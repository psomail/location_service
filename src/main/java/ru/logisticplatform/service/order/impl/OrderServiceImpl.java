package ru.logisticplatform.service.order.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.logisticplatform.model.order.Order;
import ru.logisticplatform.model.order.OrderStatus;
import ru.logisticplatform.model.user.User;
import ru.logisticplatform.repository.order.OrderRepository;
import ru.logisticplatform.service.order.OrderService;

import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public Order findById(Long id) {

        Order order = orderRepository.findById(id).orElse(null);

        if (order == null){
            log.warn("IN OrderServiceImpl findById() - no order found by id: {}", id);
            return null;
        }

        log.info("IN OrderServiceImpl findById() - order: {} found by id: {}", order, id);

        return order;
    }

    /**
     *
     * @param user
     * @param status
     * @return
     */
    @Override
    public List<Order> findAllByUserAndStatusNotLike(User user, OrderStatus status) {

        List<Order> orders = orderRepository.findAllByUserAndOrderStatusNotLike(user, status);

        if(orders.isEmpty()){
            log.warn("IN OrderServiceImpl findAllByUserAndStatusNotLike() - no order found by user: {} " +
                    "and order status not like: {}",user.getUsername(), status.toString());
        }

        return orders;
    }

    /**
     *
     * @param order
     * @return
     */
    @Override
    public Order createOrder(Order order) {

        Order createdOrder = orderRepository.save(order);

        log.info("IN OrderServiceImpl createOrder() - order id: {} successfully created", createdOrder.getId());

        return createdOrder;
    }

    @Override
    public Order updateOrder(Order order) {

        Order updatedOrder = orderRepository.save(order);

        log.info("IN OrderServiceImpl updateOrder() - order id: {} successfully updated", updatedOrder.getId());

        return updatedOrder;
    }

}
