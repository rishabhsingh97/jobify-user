package com.rsuniverse.jobify_user.config.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.rsuniverse.jobify_user.models.enums.UserRole;
import com.rsuniverse.jobify_user.models.enums.UserStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rsuniverse.jobify_user.models.pojos.AuthUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AuthFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        AuthUser user = this.extractHeaders(request, response);

        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private AuthUser extractHeaders(HttpServletRequest request, HttpServletResponse response) throws IOException {


  //   TODO  Bypass for these request .requestMatchers("/auth/**").permitAll()
        final List<String> headers = List.of(
                "id",
                "fullname",
                "email",
                "roles",
                "status"
        );

        // Check for missing headers
        List<String> missingHeaders = headers.stream()
                .filter(header -> StringUtils.isBlank(request.getHeader(header)))
                .collect(Collectors.toList());

        // Log and respond if there are missing headers
        if (!missingHeaders.isEmpty()) {
            log.warn("Missing authentication headers: {}", missingHeaders);
            return null;
        }

        // Retrieve headers
        String userId = request.getHeader("id");
        String userName = request.getHeader("fullname");
        String userEmail = request.getHeader("email");
        String userRoles = request.getHeader("roles");
        String userStatus = request.getHeader("status");

        // Process roles
        Set<UserRole> roles;
        try {
            roles = Arrays.stream(userRoles.split(","))
                    .map(String::trim)
                    .map(UserRole::valueOf)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("Invalid roles in header: {}", userRoles);
            return null;
        }

        // Create AuthUser object
        AuthUser user = new AuthUser();
        user.setId(userId);
        user.setEmail(userEmail);
        user.setFullName(userName);
        user.setRoles(roles);
        user.setStatus(UserStatus.valueOf(userStatus));

        log.info("Authenticated user: {}", user);
        return user;
    }

}
