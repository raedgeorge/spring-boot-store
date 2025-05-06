package com.codewithmosh.store.carts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddItemToCartRequest {

    @NotNull(message = "Product Id is required")
    @Positive(message = "Product Id can't be a negative number")
    Long productId;
}
