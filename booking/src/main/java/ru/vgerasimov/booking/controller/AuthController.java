package ru.vgerasimov.booking.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vgerasimov.booking.dto.AuthRequest;
import ru.vgerasimov.booking.dto.RegisterRequest;
import ru.vgerasimov.booking.dto.TokenResponse;
import ru.vgerasimov.booking.dto.UserDto;
import ru.vgerasimov.booking.mapper.UserMapper;
import ru.vgerasimov.booking.service.TokenService;
import ru.vgerasimov.booking.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final TokenService tokenService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("User registration request for: {}", request.getUsername());
        UserDto user = userService.registerUser(request);
        String token = tokenService.generateToken(userMapper.toEntity(user));
        return ResponseEntity.ok(new TokenResponse(token, "Bearer"));
    }

    @PostMapping("/auth")
    public ResponseEntity<TokenResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        log.info("User authentication request for: {}", request.getUsername());
        UserDto user = userService.authenticateUser(request.getUsername(), request.getPassword());
        String token = tokenService.generateToken(userMapper.toEntity(user));
        return ResponseEntity.ok(new TokenResponse(token, "Bearer"));
    }

}
