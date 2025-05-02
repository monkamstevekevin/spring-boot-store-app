package com.codewithmosh.store.carts;

import com.codewithmosh.store.produts.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@Tag(name = "Cart", description = "Cart API")
public class CartController {

    private final CartService cartService;

@Operation(summary = "Create a new cart")
    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cartDto = cartService.createCartDto();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @Operation(summary = "Add item to cart")
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addItemToCart(
            @PathVariable("cartId") UUID cartId,
            @RequestBody AddItemToCartRequest request,
            UriComponentsBuilder uriBuilder
    ) {

        var cartItemDto = cartService.addItemToCart(cartId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto); // optionally return something like CartItemDto
    }
    @Operation(summary = "Get cart by ID")

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);
    }

    @Operation(summary = "update cart item")
    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        var cartItemDto = cartService.updateCartItem(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartItemDto);
    }

    @Operation(summary = "Remove item from cart")
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItemFromCart(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ) {
        cartService.removeItemFromCart(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove all items from cart")
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> removeAllItemsFromCart(
            @PathVariable("cartId") UUID cartId
    ) {
        cartService.removeAllItemsFromCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFoundException(

    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFoundException(
            ProductNotFoundException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Product not found")
        );
    }


}
