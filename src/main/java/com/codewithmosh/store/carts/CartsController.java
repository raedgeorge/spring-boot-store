package com.codewithmosh.store.carts;

import com.codewithmosh.store.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartsController {

    private final CartService cartService;

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable (name = "cartId") final UUID cartId) {

        return ResponseEntity.ok(cartService.getCard(cartId));
    }

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder){

        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @Operation(summary = "Adds a product to the cart.")
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "The Id of the cart.")
            @PathVariable (name = "cartId") UUID cartId,
            @RequestBody @Valid AddItemToCartRequest request){

        CartItemDto cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(
            @PathVariable (name = "cartId") UUID cartId,
            @PathVariable (name = "productId") Long productId,
            @RequestBody @Valid UpdateCartItemRequest request){

        var cartItemDto = cartService.updateItem(cartId, productId, request.getQuantity());

        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId){

        cartService.removeFromCart(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable("cartId") UUID cartId){

        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFoundExceptions(){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Cart not found."));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFoundExceptions(){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Product not found in the cart."));
    }
}
