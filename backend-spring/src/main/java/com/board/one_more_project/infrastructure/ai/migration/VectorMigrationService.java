package com.board.one_more_project.infrastructure.ai.migration;

import com.board.one_more_project.domain.ingredient.dto.Ingredient;
import com.board.one_more_project.domain.ingredient.IngredientRepository;
import com.board.one_more_project.domain.spice.Spice;
import com.board.one_more_project.domain.spice.SpiceRepository;
import com.board.one_more_project.domain.preference.Preference;
import com.board.one_more_project.domain.preference.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VectorMigrationService {

    private final IngredientRepository ingredientRepository;
    private final SpiceRepository spiceRepository;
    private final PreferenceRepository preferenceRepository;
    private final EmbeddingModel embeddingModel;

    /**
     * [전체 마이그레이션]
     * @Transactional을 추가하여 전체 프로세스의 데이터 일관성을 보장합니다.
     */
    @Transactional
    public void migrateAll() {
        log.info("=== Ollama 벡터 마이그레이션 시작 ===");

        // 1. 재료 마이그레이션
        List<Ingredient> ingredients = ingredientRepository.findAll();
        for (Ingredient item : ingredients) {
            // Ollama는 float[]를 반환함
            float[] vector = embeddingModel.embed(item.getName());
            String vectorString = Arrays.toString(vector);
            ingredientRepository.updateEmbedding(item.getId(), vectorString);
        }
        log.info("재료 마이그레이션 완료 ({}건)", ingredients.size());

        // 2. 조미료 마이그레이션
        List<Spice> spices = spiceRepository.findAll();
        for (Spice item : spices) {
            float[] vector = embeddingModel.embed(item.getName());
            String vectorString = Arrays.toString(vector);
            spiceRepository.updateEmbedding(item.getId(), vectorString);
        }
        log.info("조미료 마이그레이션 완료 ({}건)", spices.size());

        // 3. 취향 마이그레이션
        List<Preference> preferences = preferenceRepository.findAll();
        for (Preference item : preferences) {
            float[] vector = embeddingModel.embed(item.getName());
            String vectorString = Arrays.toString(vector);
            preferenceRepository.updateEmbedding(item.getId(), vectorString);
        }
        log.info("취향 마이그레이션 완료 ({}건)", preferences.size());

        log.info("=== 모든 벡터 마이그레이션 완료 ===");
    }
}