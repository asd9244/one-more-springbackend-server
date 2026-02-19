package com.board.one_more_project.domain.preference;

import com.board.one_more_project.domain.ingredient.dto.IngredientResponse;
import com.board.one_more_project.domain.spice.SpiceResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "취향 기반 추천 키워드 (재료 + 조미료)")
public record PreferenceRecommendationResponse (
        @Schema(description = "입력한 취향과 어울리는 추천 재료 목록")
        List<IngredientResponse> ingredientResponseList,

        @Schema(description = "입력한 취향과 어울리는 추천 조미료 목록")
        List<SpiceResponse> spiceResponses

) {}




