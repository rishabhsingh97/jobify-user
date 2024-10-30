package com.rsuniverse.jobify_user.controllers;

import com.rsuniverse.jobify_user.models.enums.KafkaTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsuniverse.jobify_user.models.dtos.UserDTO;
import com.rsuniverse.jobify_user.models.pojos.AuthUser;
import com.rsuniverse.jobify_user.models.pojos.KafkaEvent;
import com.rsuniverse.jobify_user.models.responses.BaseRes;
import com.rsuniverse.jobify_user.models.responses.PaginatedRes;
import com.rsuniverse.jobify_user.services.KafkaProducerService;
import com.rsuniverse.jobify_user.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KafkaProducerService kafkaProducerService;

    @GetMapping("")
    public ResponseEntity<BaseRes<PaginatedRes<UserDTO>>> getAllUsers(@RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Incoming request to get all users");
        PaginatedRes<UserDTO> paginatedRes = userService.getAllUsers(page, size);
        return BaseRes.success(paginatedRes);
    }

    @GetMapping("/auth-user")
    public ResponseEntity<BaseRes<UserDTO>> getAuthenticatedUser(HttpServletRequest request, Authentication authentication) {
        log.info("Incoming request to get authenticated user: {}", authentication.getPrincipal());
        AuthUser user = (AuthUser) authentication.getPrincipal();
        UserDTO userDTO = userService.getUserById(user.getId());
        return BaseRes.success(userDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseRes<UserDTO>> getUserById(@PathVariable String id) {
        log.info("Incoming request to get user with id: {}", id);
        UserDTO userDTO = userService.getUserById(id);
        return BaseRes.success(userDTO);
    }

    @PostMapping("")
    public ResponseEntity<BaseRes<UserDTO>> createUser(@RequestBody @Valid UserDTO userDTO) {
        log.info("Incoming request to create user: {}", userDTO);
        UserDTO createdUser = userService.createUser(userDTO);
        kafkaProducerService.sendMessage("user_event", KafkaEvent.builder()
                .status("success")
                .type(KafkaTopic.USER_REGISTERED.getName())
                .payload(createdUser)
                .build());
        return BaseRes.success(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseRes<UserDTO>> updateUser(@PathVariable String id, @RequestBody @Valid UserDTO userDTO) {
        log.info("Incoming request to update user with id: {}, user: {}", id, userDTO);
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        kafkaProducerService.sendMessage(KafkaTopic.USER_UPDATED.getName(), KafkaEvent.builder()
                .status("success")
                .type(KafkaTopic.USER_UPDATED.getName())
                .payload(updatedUser)
                .build());
        return BaseRes.success(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseRes<Void>> deleteUser(@PathVariable String id) {
        log.info("Incoming request to delete user with id: {}", id);
        UserDTO userDto = userService.deleteUser(id);
        kafkaProducerService.sendMessage(KafkaTopic.USER_DELETED.getName(), KafkaEvent.builder()
                .status("success")
                .type(KafkaTopic.USER_DELETED.getName())
                .payload(userDto)
                .build());
        return BaseRes.success(null);
    }
}
