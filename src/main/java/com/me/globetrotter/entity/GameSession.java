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
@Table(name = "game_sessions")
public class GameSession {

    @Id
    @CustomId
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "destination_ids")
    @Convert(converter = ListToStringConverter.class)
    private List<String> destinationIds;

    private Integer currentRound;
    private Integer score;

    public synchronized void incrementCurrentRound() {
        this.currentRound++;
    }
}
