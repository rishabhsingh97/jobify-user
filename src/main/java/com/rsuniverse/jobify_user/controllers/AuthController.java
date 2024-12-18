package com.rsuniverse.jobify_user.controllers;

import com.rsuniverse.jobify_user.models.dtos.LoginDTO;
import com.rsuniverse.jobify_user.models.dtos.RefreshDTO;
import com.rsuniverse.jobify_user.models.dtos.UserDTO;
import com.rsuniverse.jobify_user.models.enums.KafkaTopic;
import com.rsuniverse.jobify_user.models.enums.UserRole;
import com.rsuniverse.jobify_user.models.pojos.AuthUser;
import com.rsuniverse.jobify_user.models.pojos.KafkaEvent;
import com.rsuniverse.jobify_user.models.responses.BaseRes;
import com.rsuniverse.jobify_user.services.AuthService;
import com.rsuniverse.jobify_user.services.KafkaProducerService;
import com.rsuniverse.jobify_user.services.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final KafkaProducerService kafkaProducerService;
    private final UserService userService;

    /**
     * Admin can register a new user with the provided fields in UserDTO
     *
     * @param userDTO - a valid user creation requestDTO
     * @return userDTO as response
     */
    @RateLimiter(name = "defaultRateLimiter")
    @PostMapping("/register")
    public ResponseEntity<BaseRes<UserDTO>> register(@RequestBody @Valid UserDTO userDTO) {
        log.info("Registering new user with details: {}", userDTO);

        UserDTO registeredUser = userService.createUser(userDTO);
        kafkaProducerService.sendMessage(KafkaTopic.USER_REGISTERED.getName(), KafkaEvent.builder()
                .status("success")
                .type("user:registered")
                .payload(registeredUser)
                .build());
        log.info("User registered successfully: {}", registeredUser);
        return BaseRes.success(registeredUser);
    }

    /**
     * Login API
     *
     * @param loginDTO - login request object
     * @return access token and refresh token for authenticated user
     */
    @RateLimiter(name = "defaultRateLimiter")
    @PostMapping("/login")
    public ResponseEntity<BaseRes<LoginDTO>> login(@RequestBody @Valid LoginDTO loginDTO) {
        log.info("User login attempt with username: {}", loginDTO.getUsername());
        LoginDTO authenticatedUser = authService.login(loginDTO, authenticationManager);
        log.info("User logged in successfully: {}", authenticatedUser.toString());
        return BaseRes.success(authenticatedUser);
    }

    @RateLimiter(name = "defaultRateLimiter")
    @GetMapping("/info/{token}")
    public ResponseEntity<BaseRes<AuthUser>> login(@PathVariable String token) {
        log.info("Get auth info attempt with token: {}", token);
        AuthUser authenticatedUser = authService.getAuthInfo(token);
        log.info("Auth user fetched successfully: {}", authenticatedUser.toString());
        return BaseRes.success(authenticatedUser);
    }

    /**
     * Refresh token API
     *
     * @param refreshDTO - refresh token request object
     * @return new access token along with refresh token (if validated)
     */
    @RateLimiter(name = "defaultRateLimiter")
    @PostMapping("/refresh")
    public ResponseEntity<BaseRes<RefreshDTO>> refreshToken(@RequestBody @Valid RefreshDTO refreshDTO) {
        log.info("Attempting to refresh token for user with refresh token: {}", refreshDTO.getRefreshToken());
        RefreshDTO refreshedToken = authService.refresh(refreshDTO);
        log.info("Token refreshed successfully for user");
        return BaseRes.success(refreshedToken);
    }
}
