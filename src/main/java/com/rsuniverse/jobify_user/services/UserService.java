package com.rsuniverse.jobify_user.services;

import java.util.List;
import java.util.stream.Collectors;

import com.rsuniverse.jobify_user.exception.customExceptions.UserException;
import com.rsuniverse.jobify_user.models.enums.ErrorCode;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rsuniverse.jobify_user.models.dtos.UserDTO;
import com.rsuniverse.jobify_user.models.entities.User;
import com.rsuniverse.jobify_user.models.responses.PaginatedRes;
import com.rsuniverse.jobify_user.repos.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "users", key = "#page + '-' + #size")
    public PaginatedRes<UserDTO> getAllUsers(int page, int size) {
        log.info("Fetching all users - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userRepo.findAll(pageable);

        List<UserDTO> users = userPage.getContent().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        return PaginatedRes.<UserDTO>builder()
                .items(users)
                .pageNum(userPage.getNumber() + 1)
                .pageSize(userPage.getSize())
                .totalPages(userPage.getTotalPages())
                .totalCount(userPage.getTotalElements())
                .build();
    }

    @Cacheable(value = "user", key = "#id")
    public UserDTO getUserById(String id) {
        log.info("Fetching user by id: {}", id);
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return modelMapper.map(user, UserDTO.class);
    }

    @CachePut(value = "user", key = "#result.id")
    public UserDTO createUser(UserDTO userDTO) {
        boolean existingUser = userRepo.existsUserByEmail(userDTO.getEmail());
        if (existingUser) {
            throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
        }
        log.info("Creating user: {}", userDTO);
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        log.info("User created successfully: {}", user);
        return modelMapper.map(user, UserDTO.class);
    }

    @CachePut(value = "user", key = "#id")
    public UserDTO updateUser(String id, UserDTO userDTO) {
        log.info("Updating user with id: {}", id);
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        User updatedUserFields = modelMapper.map(userDTO, User.class);
        User updatedUser = this.validateAndUpdateFields(user, updatedUserFields);

        log.info("User updated: {}", updatedUser);
        userRepo.save(updatedUser);
        log.info("User updated successfully: {}", user);
        return modelMapper.map(user, UserDTO.class);
    }

    private User validateAndUpdateFields(User user, User updatedUserFields) {
        if (!user.getEmail().equals(updatedUserFields.getEmail())) {
            if (userRepo.existsUserByEmail(updatedUserFields.getEmail())) {
                throw new UserException(ErrorCode.USER_ALREADY_EXISTS, "Another user already exists with this email");
            }
        }

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(updatedUserFields, user);

        if (updatedUserFields.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updatedUserFields.getPassword()));
        }
        return user;
    }

    @CacheEvict(value = "user", key = "#id")
    public UserDTO deleteUser(String id) {
        log.info("Deleting user with id: {}", id);
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + id));
        userRepo.delete(user);
        log.info("User with id {} deleted successfully", id);
        return modelMapper.map(user, UserDTO.class);
    }
}
