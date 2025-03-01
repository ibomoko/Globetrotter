package com.me.globetrotter.service;

import com.me.globetrotter.dto.GameSessionResponse;
import com.me.globetrotter.dto.GameSessionStartResponse;
import com.me.globetrotter.dto.UserInfo;
import com.me.globetrotter.entity.Destination;
import com.me.globetrotter.entity.GameSession;
import com.me.globetrotter.entity.User;
import com.me.globetrotter.error.exception.ResourceAlreadyExistsException;
import com.me.globetrotter.error.exception.ResourceNotFoundException;
import com.me.globetrotter.repository.DestinationRepository;
import com.me.globetrotter.repository.GameSessionRepository;
import com.me.globetrotter.repository.UserRepository;
import com.me.globetrotter.util.DateHelper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;

    @Resource(name = "requestScopedUser")
    UserInfo currentUser;

    public GameSessionStartResponse startGameSession() {
        GameSession ongoingGameSession = gameSessionRepository.findByUserIdAndEndDateNull(currentUser.getId());
        if (ongoingGameSession != null) {
            throw new ResourceAlreadyExistsException("User already has an ongoing game session");
        }
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found with this id"));

        GameSession gameSession = createGameSession(user);

        gameSessionRepository.save(gameSession);
        return new GameSessionStartResponse(gameSession.getId());
    }

    private GameSession createGameSession(User user) {
        List<String> randomDestinationIds = destinationRepository.findRandomDestinations();
        return GameSession.builder()
                .user(user)
                .createDate(DateHelper.getUtcNow())
                .score(0)
                .currentRound(0)
                .destinationIds(randomDestinationIds)
                .build();
    }

    public GameSessionResponse proceedGameSession(String gameSessionId, String answer) {

        // Fetch the ongoing game session for the user
        GameSession gameSession = gameSessionRepository.findByIdAndUserIdAndEndDateIsNull(gameSessionId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Game session not found"));

        // Check if it's the first round
        if (gameSession.getCurrentRound() == 0) {
            String currentDestinationId = gameSession.getDestinationIds().get(0);
            Destination currentDestination = destinationRepository.findById(currentDestinationId).
                    orElseThrow(() -> new ResourceNotFoundException("Destination not found"));

            gameSession.incrementCurrentRound();
            gameSessionRepository.save(gameSession);

            /* For the first round, no fun facts or trivia are available as the game session
             does not include a previous destination. */
            return GameSessionResponse.builder()
                    .clues(currentDestination.getClues())
                    .build();
        }

        // Retrieve the current and next destinations
        String currentDestinationId = gameSession.getDestinationIds().get(gameSession.getCurrentRound() - 1);
        String nextDestinationId = gameSession.getCurrentRound() == 5 ?
                currentDestinationId : gameSession.getDestinationIds().get(gameSession.getCurrentRound());

        Destination currentDestination = destinationRepository.findById(currentDestinationId).
                orElseThrow(() -> new ResourceNotFoundException("Current destination not found"));
        Destination nextDestination = destinationRepository.findById(nextDestinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Next destination not found"));

        // Check if the user's answer is correct
        if (answer.equalsIgnoreCase(currentDestination.getCity())) {
            gameSession.incrementScore();
        }

        // End the game if all rounds are completed
        if (isGameOver(gameSession)) {
            gameSession.setEndDate(DateHelper.getUtcNow());
            gameSessionRepository.save(gameSession);
            return GameSessionResponse.builder()
                    .score(gameSession.getScore())
                    .currentRound(gameSession.getCurrentRound())
                    .funFacts(nextDestination.getFunFacts())
                    .trivia(nextDestination.getFunFacts())
                    .build();
        }

        // Proceed to the next round
        gameSession.incrementCurrentRound();
        gameSessionRepository.save(gameSession);

        return GameSessionResponse.builder()
                .clues(nextDestination.getClues())
                .trivia(currentDestination.getTrivia())
                .funFacts(currentDestination.getFunFacts())
                .score(gameSession.getScore())
                .currentRound(gameSession.getCurrentRound())
                .build();

    }

    private boolean isGameOver(GameSession gameSession) {
        return gameSession.getCurrentRound() >= gameSession.getDestinationIds().size();
    }
}
