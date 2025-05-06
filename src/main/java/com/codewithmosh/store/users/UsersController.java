package com.codewithmosh.store.users;

import com.codewithmosh.store.common.ErrorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false, name = "sort", defaultValue = "") String sort){

        return userService.getAllUsers(sort);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder){

        var userDto = userService.registerUser(request);

        URI uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);
    }


    @PutMapping("/{id}")
    public UserDto updateUser(
            @RequestBody UpdateUserRequest request,
            @PathVariable(name = "id") Long userId){

        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id){

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequest request,
            @PathVariable(name = "id") Long id){

        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorDto> handleDuplicateUserException(DuplicateUserException exc){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(exc.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("User not found"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(AccessDeniedException exc){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(exc.getMessage()));
    }
}
