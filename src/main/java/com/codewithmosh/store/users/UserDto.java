package com.codewithmosh.store.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {

    @JsonProperty("user_id")
    private Long id;
    private String name;
    private String email;
}
