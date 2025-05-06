package com.codewithmosh.store.orders;

import com.codewithmosh.store.auth.AuthService;
import com.codewithmosh.store.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    public List<OrderDto> getAllOrders(){

        User user = authService.getCurrentUser();
        List<Order> orders = orderRepository.gerOrdersByCustomer(user);

        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {

        Order order = orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);

        User user = authService.getCurrentUser();
        if (!order.isPlacedBy(user))
            throw new AccessDeniedException("You do not have permission to access this order.");

        return orderMapper.toDto(order);
    }
}
