package com.codewithmosh.store.orders;

import com.codewithmosh.store.auths.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private AuthService authService;
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;

    public List<OrderDto> getALlOrders() {
        var user  = authService.getCurrentUser();
        var orders =   orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(
                orderMapper::toDto
        ).toList();

    }

    public OrderDto getOrder(Long orderId) {

        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(
                OrderNotFoundException::new
        );
        var user  = authService.getCurrentUser();
        if (!order.isPlacedBy(user)) {
            throw new AccessDeniedException("You don<t have acces");
        }

        return orderMapper.toDto(order);

    }
}
