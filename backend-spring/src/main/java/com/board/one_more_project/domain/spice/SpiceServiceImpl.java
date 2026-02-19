package com.board.one_more_project.domain.spice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel; // Spring AI 인터페이스
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpiceServiceImpl implements SpiceService {

    private final SpiceRepository spiceRepository;
    private final EmbeddingModel embeddingModel; // Ollama 구현체가 자동 주입됨

    @Override
    public List<SpiceResponse> getAllSpices() {
        log.info("모든 조미료 데이터 조회 요청");
        return spiceRepository.findAllByOrderByNameAsc()
                .stream()
                .map(SpiceResponse::from)
                .toList();
    }

    /**
     * [의미론적 검색]
     * Ollama를 통해 검색어를 벡터화하고 DB 유사도 쿼리를 실행합니다.
     */
    @Override
    public List<SpiceResponse> searchSpices(String keyword) {
        log.info("조미료 검색 시작 (Ollama): {}", keyword);

        // 1. 검색어 임베딩 생성
        float[] vector = embeddingModel.embed(keyword);

        // 2. pgvector 유사도 검색 수행
        List<Spice> results = spiceRepository.findNearestSpices(Arrays.toString(vector), 10);

        // 3. DTO 변환 후 반환
        return results.stream()
                .map(SpiceResponse::from)
                .toList();
    }
}