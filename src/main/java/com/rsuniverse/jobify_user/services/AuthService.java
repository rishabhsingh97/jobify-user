package com.rsuniverse.jobify_user.services;

import com.rsuniverse.jobify_user.exception.customExceptions.AuthException;
import com.rsuniverse.jobify_user.exception.customExceptions.UserException;
import com.rsuniverse.jobify_user.models.dtos.LoginDTO;
import com.rsuniverse.jobify_user.models.dtos.RefreshDTO;
import com.rsuniverse.jobify_user.models.enums.ErrorCode;
import com.rsuniverse.jobify_user.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.rsuniverse.jobify_user.models.pojos.AuthUser;
import com.rsuniverse.jobify_user.repos.UserRepo;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).map(user -> modelMapper.map(user, AuthUser.class))
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public LoginDTO login(LoginDTO loginDTO, AuthenticationManager authenticationManager) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            log.warn("Invalid credentials for user: {}", loginDTO.getPassword());
        }
        catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new AuthException(ErrorCode.AUTH_TOKEN_NOT_REFRESHED);
        }

        if (authentication !=null && authentication.isAuthenticated()) {
            AuthUser user = (AuthUser) authentication.getPrincipal();

            String accessToken = JwtUtils.generateToken(user);
            String refreshToken = JwtUtils.generateRefreshToken(user);

            loginDTO.setAccessToken(accessToken);
            loginDTO.setRefreshToken(refreshToken);

            return loginDTO;
        }

        throw new AuthException(ErrorCode.AUTH_INVALID_PASSWORD);

    }

    public RefreshDTO refresh(@Valid RefreshDTO refreshDTO) {
        String refreshToken = refreshDTO.getRefreshToken();
        String subject = JwtUtils.extractSubject(refreshToken);
        AuthUser user = JwtUtils.extractUser(refreshToken);

        if (JwtUtils.validateToken(refreshToken, subject)) {
            String newAccessToken = JwtUtils.generateToken(user);
            refreshDTO.setAccessToken(newAccessToken);
            return refreshDTO;
        }
        else {
            throw new AuthException(ErrorCode.AUTH_INVALID_USERNAME,"token not refreshed");
        }
    }

    public AuthUser getAuthInfo(String token) {
        try {
            return JwtUtils.extractUser(token);
        } catch (Exception e) {
            log.error("Error while extracting auth info: {}", e.getMessage());
            throw new AuthException(ErrorCode.AUTH_TOKEN_NOT_REFRESHED);
        }

    }
}
