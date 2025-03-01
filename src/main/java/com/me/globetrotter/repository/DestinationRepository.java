package com.me.globetrotter.repository;

import com.me.globetrotter.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, String> {

    @Query(value = "SELECT d.id FROM destinations as d ORDER BY RANDOM() LIMIT 5", nativeQuery = true)
    List<String> findRandomDestinations();
}
