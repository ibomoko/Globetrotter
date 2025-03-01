package com.me.globetrotter.dto;

import lombok.Builder;

@Builder
public record InvitationDetail(String inviterUsername,
                               Integer inviterLastScore,
                               Integer inviterMaxScore) {
}
