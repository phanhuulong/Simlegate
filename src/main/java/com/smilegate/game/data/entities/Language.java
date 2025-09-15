package com.smilegate.game.data.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

// Manage the list of supported system languages
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"createdAt"})
public class Language {
    @Id
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "is_default")
    private boolean isDefault;
    @Column(name = "created_at")

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
