package com.me.globetrotter.repository;

import com.me.globetrotter.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, String> {
    Optional<Invitation> findByIdAndIsCompletedFalse(String id);

}
