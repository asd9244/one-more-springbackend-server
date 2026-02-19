package com.board.one_more_project.domain.ingredient;

import com.board.one_more_project.domain.ingredient.dto.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// DB에 직접 접근해서 데이터를 CRUD하는 Dtat Access Layer(데이터 접근 계층).
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // JPA 활용 쿼리
    List<Ingredient> findAllByOrderByNameAsc();

    // 반환 타입을 int로 변경하고 clearAutomatically 추가하여 PSQLException 방지
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ingredients SET embedding = cast(:embedding as vector) WHERE id = :id", nativeQuery = true)
    int updateEmbedding(@Param("id") Long id, @Param("embedding") String embedding);

    @Query(value = "SELECT * FROM ingredients " +
            "ORDER BY embedding <=> cast(:queryVector as vector) " +
            "LIMIT :limit", nativeQuery = true)
    List<Ingredient> findNearestIngredients(@Param("queryVector") String queryVector, @Param("limit") int limit);
}