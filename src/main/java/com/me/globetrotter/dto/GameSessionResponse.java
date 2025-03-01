package com.me.globetrotter.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GameSessionResponse(List<String> clues,
                                  List<String> trivia,
                                  List<String> funFacts,
                                  Integer currentRound,
                                  Integer score) {
}
