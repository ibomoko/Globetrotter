package com.me.globetrotter.service;

import com.me.globetrotter.dto.AuthRequest;
import com.me.globetrotter.dto.security.TokenDetail;
import com.me.globetrotter.entity.User;
import com.me.globetrotter.error.exception.ResourceAlreadyExistsException;
import com.me.globetrotter.error.exception.ResourceNotFoundException;
import com.me.globetrotter.repository.UserRepository;
import com.me.globetrotter.security.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;


    public TokenDetail signUp(AuthRequest request) {
        userRepository.findByUsername(request.username())
                .ifPresent(user -> {
                    throw new ResourceAlreadyExistsException("User already exists with this username");
                });

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);
        return jwtProvider.generateToken(user.getId());
    }

    public TokenDetail signIn(AuthRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResourceNotFoundException("User not found");
        }
        return jwtProvider.generateToken(user.getId());
    }
}
