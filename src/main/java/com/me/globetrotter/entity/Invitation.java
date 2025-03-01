package com.me.globetrotter.entity;

import com.me.globetrotter.entity.idgenerator.CustomId;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "invitations")
public class Invitation {

    @Id
    @CustomId
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User inviter;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "is_completed")
    @Builder.Default
    private Boolean isCompleted = false;

}
