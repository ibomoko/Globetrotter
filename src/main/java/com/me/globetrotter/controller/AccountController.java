package com.me.globetrotter.controller;

import com.me.globetrotter.dto.AuthRequest;
import com.me.globetrotter.dto.security.TokenDetail;
import com.me.globetrotter.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    public TokenDetail signUp(@RequestBody @Valid AuthRequest request) {
        return accountService.signUp(request);
    }

    @PostMapping("/sign-in")
    public TokenDetail signIn(@RequestBody @Valid AuthRequest request) {
        return accountService.signIn(request);
    }

}
