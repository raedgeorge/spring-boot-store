package com.codewithmosh.store.carts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @NotNull(message = "Quantity field is required")
    @Min(value = 1, message = "Quantity minimum accepted value is 1")
    private Integer quantity;
}
