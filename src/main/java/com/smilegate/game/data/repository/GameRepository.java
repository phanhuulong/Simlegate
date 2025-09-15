package com.smilegate.game.data.repository;

import com.smilegate.game.data.entities.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.smilegate.game.Enums.StatusGame;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    boolean existsBySku(String sku);
    Optional<Game> findByIdAndDeletedAtIsNull(UUID id);
    @Query("""
            select g from Game g
            left join g.translations gt
            left join g.categories c
            where (:keyword is null or :keyword = ''
             or lower(g.sku) like lower(concat('%', :keyword, '%'))
             or lower(gt.name) like lower(concat('%', :keyword, '%'))
             or lower(c.name) like lower(concat('%', :keyword, '%')))
             and g.status = 'ACTIVE'
            group by g
            order by min(case when gt.language.code = :languageCode then 0 else 1 end), g.sku
            """)
    Page<Game> searchBySkuAndCategoryNameAndNameAndDeletedAtIsNull(@Param("keyword") String keyword, Pageable pageable, @Param("languageCode") String languageCode);

    @Query("""
            select g from Game g
            where g.id in :ids and g.status='DELETED'
            """)
    List<Game> findAllByIdInAndStatusDeleted(@Param("ids") List<UUID> ids);
    @Modifying
    @Query("UPDATE Game g SET g.status = 'DELETED' WHERE g.id IN :ids")
    int softDeleteByIds(@Param("ids") List<UUID> ids);

    Optional<Game> findByIdAndStatusEquals(UUID id, StatusGame status);
    List<Game> findAllByIdInAndStatusEquals(List<UUID> ids, StatusGame status);
}
