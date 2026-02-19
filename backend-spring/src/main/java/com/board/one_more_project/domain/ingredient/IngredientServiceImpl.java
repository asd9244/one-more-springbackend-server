package com.board.one_more_project.domain.ingredient;

import com.board.one_more_project.domain.ingredient.dto.Ingredient;
import com.board.one_more_project.domain.ingredient.dto.IngredientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel; // Spring AI 표준 인터페이스
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
// 비즈니스 로직 실제 구현 계층(business Logic Layer)
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final EmbeddingModel embeddingModel; // Ollama 구현체가 자동으로 주입됨

    @Override
    public List<IngredientResponse> getAllIngredients() {
        return ingredientRepository.findAllByOrderByNameAsc()
                .stream()
                .map(IngredientResponse::from)
                .toList();
    }

    @Override
    public List<IngredientResponse> searchIngredients(String keyword) {
        log.info("Ollama 기반 의미론적 검색 시작: {}", keyword);

        // 1. Spring AI를 이용한 로컬 임베딩 생성
        // embed() 메서드는 내부적으로 Ollama API를 호출하여 float[]를 반환함
        float[] vector = embeddingModel.embed(keyword);

        // 2. pgvector 검색을 위해 배열을 문자열 포맷으로 변환
        String vectorString = Arrays.toString(vector);

        // 3. Repository의 Native Query 호출
        List<Ingredient> results = ingredientRepository.findNearestIngredients(vectorString, 10);

        return results.stream()
                .map(IngredientResponse::from)
                .toList();
    }
}