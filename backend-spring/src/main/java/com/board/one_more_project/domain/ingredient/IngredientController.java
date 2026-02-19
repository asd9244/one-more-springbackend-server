package com.board.one_more_project.domain.ingredient;

import com.board.one_more_project.domain.ingredient.dto.IngredientResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Master Data", description = "취향, 재료, 조미료 등 마스터 데이터 조회") // Swagger 그룹 이름
@Slf4j
@RestController // JSON 데이터를 반환하는 컨트롤러임을 선언
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
// frontend의 HTTP request를 받는 API Endpoint
public class IngredientController {

    private final IngredientService ingredientService; // 서비스(주방) 연결

    @Operation(summary = "재료 목록 조회", description = "모든 재료(Ingredients) 목록을 카테고리 순으로 반환합니다.")
    @GetMapping // GET 요청을 처리합니다.
    public ResponseEntity<List<IngredientResponse>> getAllIngredients() {
        log.info("GET /api/ingredients 요청 수신");

        // 1. 서비스에게 일을 시키고 결과를 받습니다.
        List<IngredientResponse> ingredients = ingredientService.getAllIngredients();

        log.info("재료 데이터 {}개 조회 완료", ingredients.size());

        // 2. 결과(DTO 리스트)를 200 OK 신호와 함께 반환합니다.
        return ResponseEntity.ok(ingredients);
    }

    @Operation(summary = "재료 검색 (AI 유사도 기반)", description = "입력한 키워드와 의미적으로 가장 유사한 재료 10개를 반환합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<IngredientResponse>> searchIngredients(@RequestParam("q") String keyword) {
        log.info("GET /api/ingredients/search?q={} 요청 수신", keyword);

        List<IngredientResponse> results = ingredientService.searchIngredients(keyword);

        return ResponseEntity.ok(results);
    }
}