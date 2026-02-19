package com.board.one_more_project.domain.preference;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * [API 응답 DTO]
 * 유저에게 제공할 취향 선택지 목록 (Preference Entity를 단순화)
 */
@Schema(description = "취향 선택지 응답 데이터 (카테고리 + 이름)")
public record PreferenceResponse(
        @Schema(description = "취향 카테고리 (STYLE, TASTE, CONDITION)", example = "STYLE")
        String category,

        @Schema(description = "취향 이름 (한식, 매운 맛, 해장용 등)", example = "한식")
        String name
) {
    /**
     * [변환 정적 팩토리 메서드]
     * DB Entity(Preference)를 응답 DTO(PreferenceResponse)로 변환하는 로직을 정의합니다.
     * Service 계층에서 이 메서드를 호출하여 Entity -> DTO 변환을 수행합니다.
     */
    public static PreferenceResponse from(Preference entity) {
        return new PreferenceResponse(
                entity.getCategory(),
                entity.getName()
        );
    }
}