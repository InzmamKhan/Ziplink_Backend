package com.github.inzmamkhan.ziplink_backend.repository;

import com.github.inzmamkhan.ziplink_backend.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

    /**
     * Find a URL record by its generated Base62 short key.
     */
    Optional<UrlEntity> findByShortKey(String shortKey);

    /**
     * Check if a short key already exists in the database.
     */
    boolean existsByShortKey(String shortKey);

    /**
     * Atomically increment the click count and update the last accessed timestamp in PostgreSQL.
     */
    @Modifying
    @Query("UPDATE UrlEntity u SET u.clickCount = u.clickCount + :increment, u.lastAccessedAt = :lastAccessedAt WHERE u.shortKey = :shortKey")
    void incrementClickCountAndAccessTime(@Param("shortKey") String shortKey,
                                          @Param("increment") long increment,
                                          @Param("lastAccessedAt") ZonedDateTime lastAccessedAt);
}