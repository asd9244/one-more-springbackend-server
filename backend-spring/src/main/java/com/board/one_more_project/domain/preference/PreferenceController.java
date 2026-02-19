package com.board.one_more_project.domain.preference;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Master Data", description = "취향, 재료, 조미료 등 마스터 데이터 조회")
@Slf4j
@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @Operation(summary = "취향 목록 조회", description = "모든 취향 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<List<PreferenceResponse>> getAllPreferences() {
        log.info("GET /api/preferences 요청 수신");
        return ResponseEntity.ok(preferenceService.getAllPreferences());
    }

    // 취향 분석 및 연관 키워드 추천
    @Operation(summary = "취향 분석 및 연관 키워드 추천", description = "유저가 선택한 취향 리스트를 받아, 어울리는 재료와 조미료를 추천합니다.")
    @PostMapping("/analyze")
    public ResponseEntity<PreferenceRecommendationResponse> analyzePreferences(
            @Parameter(description = "유저가 선택한 취향 키워드 리스트") @RequestBody List<String> preferences
    ) {
        log.info("POST /api/preferences/analyze 요청 수신: {}", preferences);
        PreferenceRecommendationResponse result = preferenceService.recommendRelatedKeywords(preferences);
        return ResponseEntity.ok(result);
    }
}