package com.smilegate.game.data.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"createdAt", "deletedAt"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categories")
    @Builder.Default
    private Set<Game> games = new HashSet<>();

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) createdAt = new Date().toInstant() ;
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
