package com.codewithmosh.store.users;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public List<UserDto> getAllUsers(String sort) {

        if (!Set.of("name", "email").contains(sort))
            sort = "name";

        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(Long userId) {
        return userRepository
                .findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserDto registerUser(RegisterUserRequest request) {

        if(userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateUserException();

        User user = userMapper.toEntity(request);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long userId, UpdateUserRequest request) {

        var user = userRepository
                .findById(userId)
                .orElseThrow(UserNotFoundException::new);

        userMapper.update(request, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!user.getPassword().equals(request.getOldPassword()))
            throw new AccessDeniedException("Old password does not match");;

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
