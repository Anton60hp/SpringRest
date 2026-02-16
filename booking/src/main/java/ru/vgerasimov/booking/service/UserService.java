package ru.vgerasimov.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vgerasimov.booking.dto.RegisterRequest;
import ru.vgerasimov.booking.dto.UserDto;
import ru.vgerasimov.booking.entity.User;
import ru.vgerasimov.booking.mapper.UserMapper;
import ru.vgerasimov.booking.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserDto registerUser(RegisterRequest request) {
        log.info("Registering user: {}", request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.USER);

        user = userRepository.save(user);
        log.info("User registered with ID: {}", user.getId());

        return userMapper.toDto(user);
    }

    public UserDto authenticateUser(String username, String password) {
        log.info("Authenticating user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password for user: " + username);
        }

        log.info("User authenticated: {}", username);
        return userMapper.toDto(user);
    }

}