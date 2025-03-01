package com.me.globetrotter.dto.security;

import lombok.Builder;

@Builder
public record TokenDetail(String token, Long expirationDate, Long createDate) {
}
