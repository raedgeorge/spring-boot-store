package com.codewithmosh.store.carts;

import com.codewithmosh.store.products.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<CartItem> items = new LinkedHashSet<>();

    public BigDecimal getTotalPrice(){
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getCartItem(Long productId){
        return items
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public CartItem addCartItem(Product product){

        CartItem cartItem = getCartItem(product.getId());

        if (cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            items.add(cartItem);
        }

        return cartItem;
    }

    public void removeCartItem(Long productId){

        CartItem cartItem = getCartItem(productId);

        if (cartItem != null){
            cartItem.setCart(null);
            items.remove(cartItem);
        }
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }

    public void clearCart(){
        items.forEach(item -> item.setCart(null));
        items.clear();
    }
}
