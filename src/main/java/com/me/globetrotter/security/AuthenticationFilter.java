package com.me.globetrotter.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.globetrotter.constants.SecurityConstants;
import com.me.globetrotter.error.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static com.me.globetrotter.error.RestExceptionHandler.buildErrorResponse;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final JWTProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final SecurityConstants securityConstants;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(authorizationHeader)) {
            if (authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                if (StringUtils.isBlank(token)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Bearer token in Authorization Header");
                    return;
                }
                if (token.length() > 30) {
                    try {
                        String userId = jwtProvider.getUserIdByToken(token);
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userId, userDetails.getPassword(), userDetails.getAuthorities());
                        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    } catch (JWTVerificationException exc) {
                        ResponseEntity<ErrorResponse> responseEntity = buildErrorResponse("Provide valid Bearer authentication header", HttpStatus.UNAUTHORIZED);
                        response.setStatus(401);
                        response.getWriter().write(objectMapper.writeValueAsString(responseEntity.getBody()));
                        return;
                    }
                }
            } else if (authorizationHeader.startsWith("Basic ")) {
                String encodedCredentials = authorizationHeader.substring(6);
                if (StringUtils.isBlank(encodedCredentials)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Basic token in Authorization Header");
                    return;
                }
                String[] decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials), StandardCharsets.UTF_8).split(":");
                String username = decodedCredentials[0];
                String password = decodedCredentials[1];
                if (securityConstants.getUsername().equals(username) && securityConstants.getPassword().equals(password)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, "", List.of(new SimpleGrantedAuthority("ADMIN")));
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization Header");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
