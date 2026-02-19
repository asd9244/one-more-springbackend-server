package com.board.one_more_project.domain.recipe;

import com.board.one_more_project.domain.ingredient.IngredientDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

// 파이썬의 레시피 추천 결과(...추천해줘.txt) 구조에 맞춤.
// ingredients -> IngredientDto List, more(추가 재료) 필드 추가.
@Schema(description = "AI 추천 레시피 상세 정보")
public record RecipeResponse(
        @Schema(description = "요리 제목", example = "매콤 돼지고기 김치 볶음밥")
        String title,

        @Schema(description = "한 줄 요약", example = "돼지고기와 김치의 환상적인 조화!")
        String summary,

        @Schema(description = "필요한 재료 리스트")
        List<IngredientDto> ingredients,

        @Schema(description = "추가로 필요한 재료 (응용 레시피인 경우)", nullable = true)
        List<IngredientDto> more,

        @Schema(description = "조리 순서", example = "[\"1. 고기를 볶는다\", \"2. 김치를 넣는다\"]")
        List<String> recipe,

        @Schema(description = "요리 팁", example = "[\"신김치를 쓰면 설탕을 넣으세요\"]")
        List<String> tip,

        // 파이썬 응답 예시에 image, reference가 있어서 추가해둠 (없을 수도 있음)
        // nullable = Json역직렬화 할 때, 데이터가 없으면 null로 표시할 수 있다는 뜻
        @Schema(description = "참고 이미지 URL", nullable = true)
        String image,

        @Schema(description = "참고 레시피 링크", nullable = true)
        String reference
) {}