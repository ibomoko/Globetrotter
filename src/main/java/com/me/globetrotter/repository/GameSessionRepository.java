package com.me.globetrotter.repository;

import com.me.globetrotter.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, String> {
    @Query("SELECT gs FROM GameSession gs WHERE gs.user.id = :userId AND gs.endDate IS NULL")
    GameSession findByUserIdAndEndDateNull(String userId);

    @Query("SELECT gs FROM GameSession gs WHERE gs.id = :id AND gs.user.id = :userId AND gs.endDate IS NULL ")
    Optional<GameSession> findByIdAndUserIdAndEndDateIsNull(String id, String userId);

    @Query("SELECT MAX(gs.score) FROM GameSession gs WHERE gs.user.id = :userId AND gs.endDate IS NOT NULL")
    Integer findMaxScoreByUserId(String userId);

    @Query("SELECT gs.score FROM GameSession gs WHERE gs.user.id = :userId AND gs.endDate IS NOT NULL ORDER BY gs.endDate DESC LIMIT 1")
    Integer findLastScoreByUserId(String userId);
}
