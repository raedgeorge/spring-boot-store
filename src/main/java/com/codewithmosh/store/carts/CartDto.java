package com.codewithmosh.store.carts;

import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class CartDto {

    private UUID id;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private Set<CartItemDto> items = new LinkedHashSet<>();
}
