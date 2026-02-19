package com.board.one_more_project.domain.spice;

import java.util.List;

/**
 * [Service Interface]
 * 조미료 도메인의 비즈니스 로직 명세(Contract)를 정의합니다.
 * 이를 통해 OCP(Open-Closed Principle)를 준수하며 구현체를 교체할 수 있는 유연성을 확보합니다.
 */
public interface SpiceService {

    /**
     * 등록된 모든 조미료 데이터를 조회합니다.
     * @return DTO로 변환된 조미료 리스트
     */
    List<SpiceResponse> getAllSpices();

    List<SpiceResponse> searchSpices(String keyword);

}