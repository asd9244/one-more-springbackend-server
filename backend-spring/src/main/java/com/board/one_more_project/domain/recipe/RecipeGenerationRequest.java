package com.board.one_more_project.domain.recipe;

import com.board.one_more_project.domain.ingredient.IngredientDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

// 프론트엔드가 "레시피 요청하면 보낼 데이터셋. 기존에는 재료만 보냈지만, 조미료와 취향(리스트)까지 포함
@Schema(description = "레시피 생성 요청 데이터 (재료 + 조미료 + 취향)")
public record RecipeGenerationRequest(
        @Schema(description = "확정된 재료 리스트 (이름+수량)", requiredMode = Schema.RequiredMode.REQUIRED)
        List<IngredientDto> ingredients,

        @Schema(description = "보유한 조미료 리스트 (이름만)", example = "[\"소금\", \"후추\", \"간장\"]")
        List<String> spices,

        @Schema(description = "유저 취향 키워드 리스트", example = "[\"매콤한\", \"간단한\", \"한식\"]")
        List<String> preferences,

        @Schema(description = "유저 식별 ID", example = "user_01")
        String userId,
        // 프론트엔드가 추천(initial)을 원하는지, 기본 재료만(basic)원하는지, 응용 요리만(more)원하는지 알려주는 변수입니다.
        @Schema(description = "요청 작업 타입 (initial: 최초추천, basic: 기본재료만, more: 추가재료포함, real: 만개의레시피)", example = "real")
        String action


) {}