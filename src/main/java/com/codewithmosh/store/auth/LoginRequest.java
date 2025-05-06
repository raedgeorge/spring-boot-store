package com.codewithmosh.store.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email field is required")
    private String email;

    @NotBlank(message = "Password field is required")
    private String password;
}
