package com.codewithmosh.store.auth;

import com.codewithmosh.store.users.User;
import com.codewithmosh.store.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        Jwt accessToken = jwtService.generateAccessToken(user);
        Jwt refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken.toString(), refreshToken.toString());
    }

    public Jwt refreshToken(String refreshToken){

        Jwt jwt = jwtService.parseToken(refreshToken);

        if (jwt == null || jwt.isExpired())
            throw new BadCredentialsException("Invalid refresh token");

        User user = userRepository.findById(jwt.getUserId()).orElseThrow();
        return jwtService.generateAccessToken(user);
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }
}
