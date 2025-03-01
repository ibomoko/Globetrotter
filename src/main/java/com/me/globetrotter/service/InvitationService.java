package com.me.globetrotter.service;

import com.me.globetrotter.dto.InvitationResponse;
import com.me.globetrotter.dto.InvitationDetail;
import com.me.globetrotter.dto.UserInfo;
import com.me.globetrotter.dto.security.AuthRequest;
import com.me.globetrotter.dto.security.TokenDetail;
import com.me.globetrotter.entity.Invitation;
import com.me.globetrotter.entity.User;
import com.me.globetrotter.error.exception.ResourceNotFoundException;
import com.me.globetrotter.repository.GameSessionRepository;
import com.me.globetrotter.repository.InvitationRepository;
import com.me.globetrotter.repository.UserRepository;
import com.me.globetrotter.util.DateHelper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final GameSessionRepository gameSessionRepository;
    private final AccountService accountService;

    @Resource(name = "requestScopedUser")
    UserInfo currentUser;

    @Value("${app.frontend-url}")
    private String appUrl;

    public InvitationResponse createInvitation() {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this id"));

        Invitation invitation = Invitation.builder()
                .inviter(user)
                .createDate(DateHelper.getUtcNow())
                .build();

        invitationRepository.save(invitation);
        return new InvitationResponse(appUrl + invitation.getId());
    }

    public InvitationDetail getInvitationDetail(String invitationId) {
        Invitation invitation = invitationRepository.findByIdAndIsCompletedFalse(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found with this id"));

        User inviter = invitation.getInviter();

        Integer lastScore = gameSessionRepository.findLastScoreByUserId(inviter.getId());
        Integer maxScore = gameSessionRepository.findMaxScoreByUserId(inviter.getId());

        return InvitationDetail.builder()
                .inviterMaxScore(maxScore)
                .inviterLastScore(lastScore)
                .inviterUsername(inviter.getUsername())
                .build();
    }

    public TokenDetail acceptInvitation(String invitationId, AuthRequest authRequest) {
        Invitation invitation = invitationRepository.findByIdAndIsCompletedFalse(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found with this id"));

        TokenDetail tokenDetail = accountService.signUp(authRequest);

        invitation.setIsCompleted(true);
        invitationRepository.save(invitation);

        return tokenDetail;
    }
}
