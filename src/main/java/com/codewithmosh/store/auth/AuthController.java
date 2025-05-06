package com.codewithmosh.store.auth;

import com.codewithmosh.store.users.UserDto;
import com.codewithmosh.store.users.User;
import com.codewithmosh.store.users.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(request);

        var cookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(loginResponse.getAccessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
         @CookieValue(value = "refreshToken") String refreshToken
    ){
        var accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){

        User user = authService.getCurrentUser();
        if (user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException exc) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", exc.getMessage()));
    }
}
