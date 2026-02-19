package com.board.one_more_project.domain.ingredient;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "재료 정보 (이름 + 수량)")
// python fast API 서버로 보낼 재료의 이름+수량 데이터를 합치는 역할.
public record IngredientDto(
        @Schema(description = "재료 이름", example = "돼지고기")
        String ingredient,

        @Schema(description = "재료 수량 (단위 포함)", example = "200g")
        String quantity
) {}