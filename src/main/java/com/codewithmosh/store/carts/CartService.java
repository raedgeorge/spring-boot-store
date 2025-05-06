package com.codewithmosh.store.carts;

import com.codewithmosh.store.products.Product;
import com.codewithmosh.store.products.ProductNotFoundException;
import com.codewithmosh.store.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartMapper cartMapper;
    private final CartsRepository cartsRepository;
    private final ProductRepository productRepository;

    public CartDto createCart(){
        Cart cart = cartsRepository.save(new Cart());
        return cartMapper.toDto(cart);
    }

    public CartDto getCard(UUID cartId){

        Cart cart = cartsRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId){

        Cart cart = cartsRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new ProductNotFoundException();

        CartItem cartItem = cart.addCartItem(product);

        cartsRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity){

        Cart cart = cartsRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        CartItem cartItem = cart.getCartItem(productId);

        if (cartItem == null)
            throw new ProductNotFoundException();

        cartItem.setQuantity(quantity);
        cartsRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeFromCart(UUID cartId, Long productId){

        Cart cart = cartsRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        cart.removeCartItem(productId);
        cartsRepository.save(cart);
    }

    public void clearCart(UUID cartId){

        Cart cart = cartsRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        cart.clearCart();
        cartsRepository.save(cart);
    }
}
