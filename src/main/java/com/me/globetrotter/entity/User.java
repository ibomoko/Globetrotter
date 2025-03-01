package com.me.globetrotter.entity;


import com.me.globetrotter.entity.idgenerator.CustomId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @CustomId
    private String id;
    private String username;
    private String password;

    @Column(name = "games_count")
    private Integer gamesCount;

    public synchronized void incrementGamesCount() {
        this.gamesCount++;
    }
}
