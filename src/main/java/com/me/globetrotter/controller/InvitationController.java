package com.me.globetrotter.controller;

import com.me.globetrotter.dto.InvitationDetail;
import com.me.globetrotter.dto.InvitationResponse;
import com.me.globetrotter.dto.security.AuthRequest;
import com.me.globetrotter.dto.security.TokenDetail;
import com.me.globetrotter.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/invitations")
    public InvitationResponse createInvitation() {
        return invitationService.createInvitation();
    }

    @GetMapping("/public/invitations/{invitationId}")
    public InvitationDetail getInvitationDetails(@PathVariable String invitationId) {
        return invitationService.getInvitationDetail(invitationId);
    }

    @PostMapping("/public/invitations/{invitationId}")
    public TokenDetail acceptInvitation(@PathVariable String invitationId,
                                        @RequestBody @Valid AuthRequest authRequest) {
        return invitationService.acceptInvitation(invitationId, authRequest);
    }

}
