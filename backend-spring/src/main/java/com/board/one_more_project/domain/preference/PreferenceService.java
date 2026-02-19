package com.board.one_more_project.domain.preference;

import java.util.List;

// 유저의 취향 선택지(Preference) 데이터를 조회하는 비즈니스 로직을 정의.
public interface PreferenceService {

    // 모든 취향 데이터를 카테고리별로 정렬하여 조회, @return 카테고리별로 정렬된 PreferenceResponse DTO 리스트
    List<PreferenceResponse> getAllPreferences();

    // 유저가 선택한 취향 리스트를 기반으로 연관된 재료와 조미료 추천
    PreferenceRecommendationResponse recommendRelatedKeywords(List<String> preferences);
}