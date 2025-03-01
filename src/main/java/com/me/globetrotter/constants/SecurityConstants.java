package com.me.globetrotter.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class SecurityConstants {

    @Value("${security.auth.whitelist}")
    private String[] whitelist;

    @Value("${security.jwt.secret}")
    private String JWTSecret;

    @Value("${security.jwt.default-expiration-time}")
    private int JWTDefaultExpirationTime;

    @Value("${security.jwt.issuer}")
    private String JWTIssuer;

    @Value("${security.basic.username}")
    private String username;

    @Value("${security.basic.password}")
    private String password;

    @Value("${security.server}")
    private String server;


}