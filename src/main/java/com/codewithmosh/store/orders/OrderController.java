package com.codewithmosh.store.orders;

import com.codewithmosh.store.common.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {

        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable(name = "orderId") Long orderId) {

        return ResponseEntity.ok(orderService.getOrder(orderId));
    }


    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleOrderNotFoundException(OrderNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(exc.getMessage()));
    }
}
