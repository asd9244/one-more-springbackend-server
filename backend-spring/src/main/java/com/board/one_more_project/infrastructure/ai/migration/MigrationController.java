package com.board.one_more_project.infrastructure.ai.migration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/migration")
@RequiredArgsConstructor
public class MigrationController {

    private final VectorMigrationService migrationService;

    // application.properties에서 관리자 키를 가져옴
    @Value("${admin.secret-key:default-unsafe-key}")
    private String adminSecretKey;

    @Operation(summary = "데이터 임베딩 초기화 (관리자 전용)",
            description = "헤더에 정확한 X-ADMIN-KEY가 있어야만 실행됩니다.")
    @PostMapping("/vectors")
    public ResponseEntity<String> runMigration(
            @Parameter(description = "관리자 비밀키", required = true)
            @RequestHeader("X-ADMIN-KEY") String requestKey
    ) {
        // 1. 보안 체크: 헤더로 들어온 키와 설정 파일의 키가 일치하는지 확인
        if (!adminSecretKey.equals(requestKey)) {
            log.warn("마이그레이션 접근 거부: 잘못된 키 ({})", requestKey);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("접근 권한이 없습니다. (Invalid Admin Key)");
        }

        log.info("관리자 인증 성공. 벡터 마이그레이션을 시작합니다.");

        // 2. 작업 수행 (비동기로 처리하면 더 좋지만, 일단 동기 처리 유지)
        try {
            migrationService.migrateAll();
            return ResponseEntity.ok("마이그레이션이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            log.error("마이그레이션 실패", e);
            return ResponseEntity.internalServerError()
                    .body("마이그레이션 중 오류 발생: " + e.getMessage());
        }
    }
}