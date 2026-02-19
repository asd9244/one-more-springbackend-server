package com.board.one_more_project.infrastructure.ai;

import com.board.one_more_project.domain.ingredient.dto.IngredientAnalysisResponse;
import com.board.one_more_project.domain.recipe.RecipeGenerationRequest;
import com.board.one_more_project.domain.recipe.RecipeResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * [AI 통신 서비스 인터페이스]
 * 컨트롤러와 실제 구현체(Real/Mock) 사이의 연결 고리입니다.
 * 파이썬 서버의 응답 구조가 { "result": [...] } 리스트 형태이므로,
 * 반환 타입도 모두 List<> 형태로 변경했습니다.
 */
public interface AiClientService {
    // 재료 사진 분석
    List<IngredientAnalysisResponse> analyzeImageIngredients(List<MultipartFile> files, String userId);

    // 영수증 사진 분석
    List<IngredientAnalysisResponse> analyzeImageReceipt(List<MultipartFile> files, String userId);

    // 최초 레시피 추천 생성 (/recipes-generate-initial)
    List<RecipeResponse> generateRecipeInitial(RecipeGenerationRequest request);

    // 기본 재료 레시피만 생성(/recipes-generate-basic)
    List<RecipeResponse> generateRecipeBasic(RecipeGenerationRequest request);

    // 추가 재료(응용) 레시피만 생성 (/recipes-generate-more)
    List<RecipeResponse> generateRecipeMore(RecipeGenerationRequest request);

    // 만개의 레시피 기반 추천 (/recipes-generate-real)
    List<RecipeResponse> generateRecipeReal(RecipeGenerationRequest request);


}