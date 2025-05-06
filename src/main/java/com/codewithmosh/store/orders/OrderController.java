package com.codewithmosh.store.orders;

import com.codewithmosh.store.common.ErrorDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping()
    public List<OrderDto> getAllOrders() {
        return orderService.getALlOrders();

    }
    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable("orderId") Long orderId) {
        return orderService.getOrder(orderId);
    }
    @ExceptionHandler({OrderNotFoundException.class})
    public ResponseEntity<Void> handleOrderNotFoundException(Exception ex) {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler({ AccessDeniedException.class})
    public ResponseEntity<ErrorDto> handleAccessDeniedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(ex.getMessage()));
    }

}
