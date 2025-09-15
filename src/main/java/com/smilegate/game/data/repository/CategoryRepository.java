package com.smilegate.game.data.repository;

import com.smilegate.game.data.entities.Category;
import com.smilegate.game.domain.model.BaseResponse;
import com.smilegate.game.domain.model.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
    boolean existsByIdAndDeletedAtIsNull(UUID id);
    @Query("""
            Select c from Category c
            where (:keyword is null or :keyword = ''
             or lower(c.name) like lower(concat('%', :keyword, '%')) 
             or lower(c.code) like lower(concat('%', :keyword, '%')))
            and c.deletedAt is null
            """)
    Page<Category> searchByNameAndCodeAndDeletedAtIsNull(@Param("keyword") String keyword, Pageable pageable);
    Category getByIdAndDeletedAtIsNull(UUID id);

    @Modifying
    @Query("UPDATE Category c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id IN :ids")
    int softDeleteByIds(@Param("ids") List<UUID> ids);

    @Query("""
            select c from Category c
            where c.id in :ids and c.deletedAt is null
            """)
    List<Category> findAllByIdInAndDeletedAtIsNull(List<UUID> ids);
    boolean existsByCodeAndIdNot(String code, UUID id);
}
