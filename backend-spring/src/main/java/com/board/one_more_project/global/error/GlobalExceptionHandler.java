package com.board.one_more_project.global.error;

import com.board.one_more_project.domain.recipe.RecipeResponse;
import com.board.one_more_project.global.error.exception.AiServerException; // import 추가
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 기존의 검증 에러 처리 (400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RecipeResponse> handleValidationException(IllegalArgumentException e) {
        log.warn("검증 실패(User Input Error): {}", e.getMessage());
        RecipeResponse errorResponse = createErrorResponse("입력 오류 발생", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // [추가됨] AI 서버 통신 에러 처리 (503 Service Unavailable)
    @ExceptionHandler(AiServerException.class)
    public ResponseEntity<RecipeResponse> handleAiServerException(AiServerException e) {
        log.error("AI 서비스 장애: {}", e.getMessage());

        // 프론트엔드가 깨지지 않도록, 에러 메시지를 담은 객체 생성
        RecipeResponse errorResponse = createErrorResponse(
                "서비스 일시 지연",
                e.getMessage() // "현재 AI 서버에 연결할 수 없습니다..." 등의 메시지가 들어감
        );

        // 503 상태 코드로 반환 -> 클라이언트가 재시도 로직을 짤 수 있음
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    // 에러 응답 생성 헬퍼 메서드 (중복 제거용)
    private RecipeResponse createErrorResponse(String title, String message) {
        return new RecipeResponse(
                title,                          // title
                message,                        // summary (에러 상세 내용)
                Collections.emptyList(),        // ingredients
                null,                           // more
                List.of("잠시 후 다시 시도해주세요."), // recipe
                List.of("서버 점검 중일 수 있습니다."), // tip
                null,                           // image
                null                            // reference
        );
    }
}