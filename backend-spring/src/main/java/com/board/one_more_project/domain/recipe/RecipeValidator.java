package com.board.one_more_project.domain.recipe;

import com.board.one_more_project.domain.ingredient.IngredientDto;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Component
public class RecipeValidator {

    // 1단계: 이미지 분석 요청 검증
    public void validateFiles(List<MultipartFile> files) {
        // 1. 파일이 아예 없는지 확인
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일이 없습니다.");
        }

        // 2. 파이썬 서버 안정성을 위해 최대 3장으로 제한
        if (files.size() > 3) {
            throw new IllegalArgumentException("사진은 최대 3장까지만 업로드 가능합니다.");
        }
    }

    // 2단계: 레시피 생성 요청 검증
    public void validateIngredients(List<IngredientDto> ingredients, List<String> preferences) {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException("입력된 재료가 없습니다.");
        }

        // 재료 이름이 비어있는지 확인
        boolean hasValidIngredient = ingredients.stream()
                .anyMatch(item -> item.ingredient() != null && !item.ingredient().trim().isEmpty());

        if (!hasValidIngredient) {
            throw new IllegalArgumentException("유효한 재료 이름이 하나도 없습니다.");
        }
        checkPreferenceLength(preferences);
    }

    // 공통: 취향 키워드 개수 제한
    private void checkPreferenceLength(List<String> preferences) {
        if (preferences != null && preferences.size() >= 100) {
            throw new IllegalArgumentException("요리 취향 키워드는 100개 이내로 선택해주세요.");
        }
    }
}