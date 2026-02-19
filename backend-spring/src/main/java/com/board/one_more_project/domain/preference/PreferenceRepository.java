package com.board.one_more_project.domain.preference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    List<Preference> findByCategoryOrderByIdAsc(String category);

    List<Preference> findAllByOrderByCategoryAscIdAsc();

    // 테이블명을 preferences로 정확히 수정 및 반환 타입 int 변경
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE preferences SET embedding = cast(:embedding as vector) WHERE id = :id", nativeQuery = true)
    int updateEmbedding(@Param("id") Long id, @Param("embedding") String embedding);
}