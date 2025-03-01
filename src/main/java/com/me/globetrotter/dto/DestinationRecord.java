package com.me.globetrotter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DestinationRecord {
    private String city;
    private String country;
    private List<String> clues;
    @JsonProperty("fun_fact")
    private List<String> funFacts;
    private List<String> trivia;
}
