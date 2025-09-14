package com.smilegate.game.data.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"game", "language"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"game_id", "language_id"}))
public class GameTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false, updatable = false)
    private Game game;
    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "short_description", nullable = true, length = 512)
    private String shortDescription;
    @Column(name = "long_description", nullable = true, length = 5000)
    private String longDescription;
}
