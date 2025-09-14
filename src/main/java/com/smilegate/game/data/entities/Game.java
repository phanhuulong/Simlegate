package com.smilegate.game.data.entities;

import com.smilegate.game.Enums.StatusGame;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"createdAt", "deletedAt"})
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(name = "sku", nullable = true)
    private String sku;
    @Enumerated(EnumType.STRING)
    private StatusGame status;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<GameTranslation> translations = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "game_category", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Builder.Default
    private Set<Category> categories = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "game_images", joinColumns = @JoinColumn(name = "game_id"))
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @Column(name = "created_by", nullable = false)
    private String createBy;
    private Instant createdAt;
    @Column(name = "updated_by", nullable = true)
    private String updateBy;
    private Instant updatedAt;
    private Instant deletedAt;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) createdAt = new Date().toInstant();
        if (this.updatedAt == null) updatedAt = new Date().toInstant();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = new Date().toInstant();
    }

    @PreRemove
    protected void preRemove() {
        this.deletedAt = new Date().toInstant();
    }
}
