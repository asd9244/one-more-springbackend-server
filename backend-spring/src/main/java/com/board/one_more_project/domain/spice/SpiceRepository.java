package com.board.one_more_project.domain.spice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface SpiceRepository extends JpaRepository<Spice, Long> {

    List<Spice> findAllByOrderByNameAsc();

    // 테이블명을 spices로 정확히 수정 및 반환 타입 int 변경
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE spices SET embedding = cast(:embedding as vector) WHERE id = :id", nativeQuery = true)
    int updateEmbedding(@Param("id") Long id, @Param("embedding") String embedding);

    @Query(value = "SELECT * FROM spices " +
            "ORDER BY embedding <=> cast(:queryVector as vector) " +
            "LIMIT :limit", nativeQuery = true)
    List<Spice> findNearestSpices(@Param("queryVector") String queryVector, @Param("limit") int limit);
}