package com.me.globetrotter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.globetrotter.dto.DestinationRecord;
import com.me.globetrotter.entity.Destination;
import com.me.globetrotter.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final ObjectMapper objectMapper;
    private final DestinationRepository destinationRepository;

    public void importDestinationRecords(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            List<DestinationRecord> destinationRecords = objectMapper.readValue(inputStream, new TypeReference<>() {
            });

            List<Destination> destinations = destinationRecords.stream().map(dr ->
                    Destination.builder()
                            .city(dr.getCity())
                            .clues(dr.getClues())
                            .country(dr.getCountry())
                            .trivia(dr.getTrivia())
                            .funFacts(dr.getFunFacts())
                            .build()
            ).toList();

            destinationRepository.saveAll(destinations);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
