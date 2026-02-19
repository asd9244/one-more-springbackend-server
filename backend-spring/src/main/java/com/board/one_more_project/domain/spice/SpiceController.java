package com.board.one_more_project.domain.spice;

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

@Tag(name = "Master Data", description = "취향, 재료, 조미료 등 마스터 데이터 조회")
@Slf4j
@RestController
@RequestMapping("/api/spices")
@RequiredArgsConstructor
public class SpiceController {

    private final SpiceService spiceService;

    @Operation(summary = "조미료 목록 조회", description = "모든 조미료 목록을 이름순으로 반환합니다.")
    @GetMapping
    public ResponseEntity<List<SpiceResponse>> getAllSpices() {
        log.info("GET /api/spices 요청 수신");
        return ResponseEntity.ok(spiceService.getAllSpices());
    }

    @Operation(summary = "조미료 검색 (AI 유사도)", description = "키워드와 유사한 조미료를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<SpiceResponse>> searchSpices(@RequestParam("q") String keyword) {
        log.info("GET /api/spices/search?q={} 요청 수신", keyword);
        return ResponseEntity.ok(spiceService.searchSpices(keyword));
    }
}