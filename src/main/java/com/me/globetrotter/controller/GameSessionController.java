package com.me.globetrotter.controller;

import com.me.globetrotter.dto.GameSessionResponse;
import com.me.globetrotter.dto.GameSessionStartResponse;
import com.me.globetrotter.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/game-sessions")
@RequiredArgsConstructor
public class GameSessionController {

    private final GameSessionService gameSessionService;

    @PostMapping("/start")
    public GameSessionStartResponse startGameSession() {
        return gameSessionService.startGameSession();
    }

    @PostMapping("/{game-session-id}/proceed")
    public GameSessionResponse proceedGameSession(@PathVariable("game-session-id") String gameSessionId,
                                                  @RequestParam(required = false) String answer) {
        return gameSessionService.proceedGameSession(gameSessionId, answer);
    }




}
