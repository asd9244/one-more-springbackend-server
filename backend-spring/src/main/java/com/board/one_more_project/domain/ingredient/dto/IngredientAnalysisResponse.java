package com.board.one_more_project.domain.ingredient.dto;

import com.board.one_more_project.domain.ingredient.IngredientDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

// 파이썬의 이미지 분석 결과(이미지 분석.txt) 구조에 맞춤. 기존의 단순 리스트에서 image_index를 포함한 구조로 변경.
@Schema(description = "AI 이미지 분석 결과 (개별 이미지)")
public record IngredientAnalysisResponse(
        @Schema(description = "이미지 번호 (0부터 시작)", example = "0")
        int image_index,

        @Schema(description = "분석된 재료 리스트")
        List<IngredientDto> ingredients
) {}