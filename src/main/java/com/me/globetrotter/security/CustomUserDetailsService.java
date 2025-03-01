package com.me.globetrotter.security;

import com.me.globetrotter.entity.User;
import com.me.globetrotter.error.exception.ResourceNotFoundException;
import com.me.globetrotter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this id"));

        return new org.springframework.security.core.userdetails
                .User(user.getId(), "", List.of(new SimpleGrantedAuthority("USER")));
    }


}
