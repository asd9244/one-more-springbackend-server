package com.board.one_more_project.domain.ingredient.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// 프론트엔드에서 사용자가 재료를 선택할 때 보여줄 리스트 아이템
@Schema(description = "재료 마스터 데이터 (카테고리 + 이름)")
public record IngredientResponse(
        @Schema(description = "재료 고유 ID", example = "1")
        Long id,

        @Schema(description = "재료 이름", example = "돼지고기")
        String name
) {
    // Entity -> DTO 변환 메서드 (정적 팩토리 메서드 패턴)
    public static IngredientResponse from(Ingredient entity) {
        return new IngredientResponse(
                entity.getId(),       // 나중에 프론트에서 ID로 제어할 수도 있어서 추가함
                entity.getName()
        );
    }
}