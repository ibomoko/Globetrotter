package com.me.globetrotter.dto.security;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AuthRequest(@NotEmpty(message = "Password can not be empty")
                                String username,
                          @NotEmpty(message = "Password can not be empty")
                                @Size(max = 15, message = "Password must be less than 15 characters")
                                String password) {
}
