package com.board.one_more_project.domain.spice;

import io.swagger.v3.oas.annotations.media.Schema;

// Java 17 record: 불변 데이터 객체(DTO)를 위한 보일러플레이트 코드 감소
@Schema(description = "조미료 마스터 데이터")
public record SpiceResponse(
        @Schema(description = "조미료 고유 ID", example = "1")
        Long id,

        @Schema(description = "조미료 이름", example = "소금")
        String name
) {
    /**
     * [Factory Method]
     * 엔티티를 DTO로 변환하는 정적 팩토리 메서드입니다.
     * 스트림 API의 map 메서드에서 메서드 레퍼런스(SpiceResponse::from)로 사용하기 적합합니다.
     */
    public static SpiceResponse from(Spice entity) {
        return new SpiceResponse(
                entity.getId(),
                entity.getName()
        );
    }
}