package com.smilegate.game.data.repository;

import com.smilegate.game.data.entities.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
    Optional<Language> findByCodeAndDeletedAtIsNull(String code);
    List<Language> findAllByCodeInAndDeletedAtIsNull(List<String> code);
    @Modifying
    @Query("UPDATE Language l SET l.deletedAt = CURRENT_TIMESTAMP WHERE l.code IN :codes")
    int softDeleteByCodes(@Param("codes") List<String> codes);
    boolean existsByIsDefaultTrue(); // get language where isDefault = true
    List<Language> findAllByDeletedAtIsNull();
    @Query("""
            select l from Language l
            where (:keyword is null or :keyword = ''
             or lower(l.name) like lower(concat('%', :keyword, '%')) 
             or lower(l.code) like lower(concat('%', :keyword, '%')))
             and l.deletedAt is null
            """)
    Page<Language> searchByNameAndCodeAndDeletedAtIsNull(@Param("keyword") String keyword, Pageable pageable);
    Optional<Language> findByIsDefaultTrueAndDeletedAtIsNull();
}
