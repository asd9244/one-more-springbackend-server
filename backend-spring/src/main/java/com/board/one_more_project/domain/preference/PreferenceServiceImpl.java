package com.board.one_more_project.domain.preference;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.board.one_more_project.domain.ingredient.dto.Ingredient;
import com.board.one_more_project.domain.ingredient.IngredientRepository;
import com.board.one_more_project.domain.ingredient.dto.IngredientResponse;
import com.board.one_more_project.domain.spice.Spice;
import com.board.one_more_project.domain.spice.SpiceRepository;
import com.board.one_more_project.domain.spice.SpiceResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PreferenceServiceImpl implements PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final EmbeddingModel embeddingModel;
    private final ChatModel chatModel;
    private final SpiceRepository spiceRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    public List<PreferenceResponse> getAllPreferences() {
        log.info("모든 취향 데이터 조회 요청 시작");
        return preferenceRepository.findAllByOrderByCategoryAscIdAsc()
                .stream()
                .map(PreferenceResponse::from)
                .toList();
    }

    @Override
    public PreferenceRecommendationResponse recommendRelatedKeywords(List<String> preferences) {
        log.info("RAG 기반 연관 키워드 추천 시작. 입력 취향: {}", preferences);

        String userPreferences = String.join(", ", preferences);

        // [개선 1] 프롬프트 강화: 부정 명령어(Negative Constraint) 추가
        // "설명하지마", "인사하지마" 등을 명시하여 사족을 방지합니다.
        String instruction = String.format(
                "너는 데이터 추출기야. 사용자의 취향 [%s]에 가장 잘 어울리는 식재료 10개와 조미료 5개를 추천해줘.\n" +
                        "규칙 1: 서론, 결론, 부가 설명, 인사말을 절대 하지 마.\n" +
                        "규칙 2: 오직 단어만 쉼표(,)로 구분해서 나열해.\n" +
                        "규칙 3: 번호 매기기(1.)나 특수문자를 쓰지 마.\n" +
                        "예시: 돼지고기, 양파, 대파, 고추장, 간장, 소금",
                userPreferences
        );

        log.info("Exaone 모델에게 질문 전송 중...");

        // 2. Exaone 호출
        String llmResponse = chatModel.call(instruction);
        log.info("Exaone 답변 수신: {}", llmResponse);

        // [개선 2] 파싱 로직 강화 (별도 메서드로 분리)
        List<String> recommendedWords = parseLlmResponse(llmResponse);

        List<IngredientResponse> finalIngredients = new ArrayList<>();
        List<SpiceResponse> finalSpices = new ArrayList<>();

        // 4. 벡터 검색 (기존 로직 유지)
        for (String word : recommendedWords) {
            // 빈 문자열이거나 너무 긴 문장(오류)은 스킵
            if (word.isBlank() || word.length() > 20) continue;

            log.debug("추천 단어 '{}'로 DB 검색 수행", word);

            float[] vector = embeddingModel.embed(word);
            String vectorString = Arrays.toString(vector);

            List<Ingredient> ingredients = ingredientRepository.findNearestIngredients(vectorString, 1);
            if (!ingredients.isEmpty()) {
                finalIngredients.add(IngredientResponse.from(ingredients.get(0)));
            }

            List<Spice> spices = spiceRepository.findNearestSpices(vectorString, 1);
            if (!spices.isEmpty()) {
                finalSpices.add(SpiceResponse.from(spices.get(0)));
            }
        }

        // 5. 중복 제거
        List<IngredientResponse> distinctIngredients = finalIngredients.stream().distinct().collect(Collectors.toList());
        List<SpiceResponse> distinctSpices = finalSpices.stream().distinct().collect(Collectors.toList());

        log.info("최종 추천 완료: 재료 {}건, 조미료 {}건", distinctIngredients.size(), distinctSpices.size());

        return new PreferenceRecommendationResponse(distinctIngredients, distinctSpices);
    }

    /**
     * [Helper Method] LLM 응답 정제 로직
     * 특수문자 제거 및 사족 필터링을 수행합니다.
     */
    private List<String> parseLlmResponse(String response) {
        if (response == null || response.isBlank()) {
            return List.of();
        }

        // 1단계: 줄바꿈 문자를 쉼표로 치환 (혹시 엔터로 구분했을 경우 대비)
        String standardized = response.replace("\n", ",");

        // 2단계: 콜론(:)이 있다면, 콜론 뒤의 텍스트만 사용 (예: "추천 목록: 사과, 배" -> " 사과, 배")
        if (standardized.contains(":")) {
            String[] parts = standardized.split(":");
            if (parts.length > 1) {
                standardized = parts[1]; // 뒷부분만 취함
            }
        }

        // 3단계: 쉼표로 분리 후 정제
        return Arrays.stream(standardized.split(","))
                .map(String::trim)                 // 앞뒤 공백 제거
                .map(this::removeSpecialCharacters)// 특수문자(번호, 괄호 등) 제거
                .filter(word -> !word.isBlank())   // 빈 문자열 제거
                .filter(word -> word.length() >= 2)// 한 글자짜리(오타 가능성) 필터링 (선택사항)
                .toList();
    }

    // 한글, 영문, 숫자, 공백을 제외한 모든 특수문자 제거
    private String removeSpecialCharacters(String input) {
        return input.replaceAll("[^가-힣a-zA-Z0-9\\s]", "");
    }
}