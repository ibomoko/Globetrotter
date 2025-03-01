package com.me.globetrotter.config;


import com.me.globetrotter.dto.UserInfo;
import com.me.globetrotter.entity.User;
import com.me.globetrotter.error.exception.ResourceNotFoundException;
import com.me.globetrotter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
@RequiredArgsConstructor
public class RequestScopedUserProvider {

    private final UserRepository userRepository;

    @RequestScope
    @Bean
    public UserInfo requestScopedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            return null;

        String userId = authentication.getName();

        if (StringUtils.isEmpty(userId))
            return null;

        User user =  userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        return UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
