package com.me.globetrotter.entity;

import com.me.globetrotter.entity.attributeconverter.ListToStringConverter;
import com.me.globetrotter.entity.idgenerator.CustomId;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "destinations")
public class Destination {

    @Id
    @CustomId
    private String id;

    private String city;
    private String country;

    @Column(name = "clues")
    @Convert(converter = ListToStringConverter.class)
    List<String> clues;

    @Column(name = "fun_fact")
    @Convert(converter = ListToStringConverter.class)
    List<String> funFacts;

    @Convert(converter = ListToStringConverter.class)
    List<String> trivia;
}
