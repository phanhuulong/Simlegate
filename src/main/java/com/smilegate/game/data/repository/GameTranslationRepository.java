package com.smilegate.game.data.repository;

import com.smilegate.game.data.entities.GameTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameTranslationRepository extends JpaRepository<GameTranslation, UUID> {
}
