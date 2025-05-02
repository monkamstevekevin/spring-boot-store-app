package com.codewithmosh.store.payments;

import com.codewithmosh.store.dto.ErrorDto;

import com.codewithmosh.store.carts.CartEmptyException;
import com.codewithmosh.store.carts.CartNotFoundException;

import com.codewithmosh.store.orders.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
 private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;




    @PostMapping()
    public CheckoutResponse checkout( @Valid @RequestBody CheckoutRequest request) {

    return  checkoutService.checkout(request);

    }
    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody String payload,
                                         @RequestHeader Map<String,String> headers) {
checkoutService.handleWebhookEvent(new com.codewithmosh.store.payments.WebhookRequest(headers,payload));

    }
    @ExceptionHandler({
            CartNotFoundException.class,
            CartEmptyException.class
    })
    public ResponseEntity<ErrorDto>handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
    @ExceptionHandler({PaymentException.class})
    public ResponseEntity<?>handlePaymentException(PaymentException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(ex.getMessage()));
    }
}
